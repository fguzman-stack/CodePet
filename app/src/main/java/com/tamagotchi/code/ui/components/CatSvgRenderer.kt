package com.tamagotchi.code.ui.components

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Picture
import android.graphics.RadialGradient
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import androidx.core.graphics.PathParser
import org.w3c.dom.Element
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory
import org.xml.sax.InputSource

/** Small, cached SVG adapter for the primitives in the author's artwork.
 * Paths are read verbatim; object-bounding-box gradients keep their elliptical
 * transforms. Parsing and recording happen once per phase, never per frame.
 */
internal class CatSvgRenderer(document: String) {
    data class Layer(val part: String, val clothingBefore: Boolean, val hatAfter: Boolean, val picture: Picture)

    private val root = DocumentBuilderFactory.newInstance().apply {
        isNamespaceAware = true
    }.newDocumentBuilder().parse(InputSource(StringReader(document))).documentElement
    private val definitions = root.children().first { it.localName == "defs" }.children().associateBy { it.getAttribute("id") }
    private val group = root.children().first { it.localName == "g" }
    private val inherited = Style().inherit(group)

    val layers: List<Layer> = group.children().map { element ->
        val picture = Picture()
        val canvas = picture.beginRecording(240, 280)
        canvas.translate(120f, 240f)
        drawElement(canvas, element, inherited)
        picture.endRecording()
        Layer(element.getAttribute("data-part"), element.getAttribute("data-clothing-before") == "true",
            element.getAttribute("data-hat-after") == "true", picture)
    }

    private data class Style(
        val fill: String = "#000000", val stroke: String = "none", val strokeWidth: Float = 1f,
        val fillOpacity: Float = 1f, val strokeOpacity: Float = 1f,
        val cap: Paint.Cap = Paint.Cap.BUTT, val join: Paint.Join = Paint.Join.MITER,
        val fontSize: Float = 12f, val fontFamily: String = "sans-serif", val bold: Boolean = false,
        val anchor: Paint.Align = Paint.Align.LEFT
    ) {
        fun inherit(e: Element) = copy(
            fill = e.attr("fill", fill), stroke = e.attr("stroke", stroke),
            strokeWidth = e.number("stroke-width", strokeWidth),
            fillOpacity = e.number("fill-opacity", fillOpacity), strokeOpacity = e.number("stroke-opacity", strokeOpacity),
            cap = when (e.getAttribute("stroke-linecap")) { "round" -> Paint.Cap.ROUND; "square" -> Paint.Cap.SQUARE; "butt" -> Paint.Cap.BUTT; else -> cap },
            join = when (e.getAttribute("stroke-linejoin")) { "round" -> Paint.Join.ROUND; "bevel" -> Paint.Join.BEVEL; "miter" -> Paint.Join.MITER; else -> join },
            fontSize = e.number("font-size", fontSize), fontFamily = e.attr("font-family", fontFamily),
            bold = if (e.hasAttribute("font-weight")) (e.getAttribute("font-weight").toIntOrNull() ?: 400) >= 600 else bold,
            anchor = when (e.getAttribute("text-anchor")) { "middle" -> Paint.Align.CENTER; "end" -> Paint.Align.RIGHT; "start" -> Paint.Align.LEFT; else -> anchor }
        )
    }

    private fun drawElement(canvas: Canvas, e: Element, parent: Style) {
        val save = canvas.save()
        applyTransform(canvas, e.getAttribute("transform"))
        val opacity = e.number("opacity", 1f)
        val alphaSave = if (opacity < 1f) canvas.saveLayerAlpha(null, (opacity * 255).toInt()) else null
        val style = parent.inherit(e)
        when (e.localName) {
            "g" -> e.children().forEach { drawElement(canvas, it, style) }
            "use" -> definitions[e.getAttribute("href").removePrefix("#")]?.let { drawElement(canvas, it, style) }
            "text" -> {
                val paint = paint(style.fill, RectF(), style.fillOpacity).apply {
                    textSize = style.fontSize
                    typeface = Typeface.create(if (style.fontFamily.contains("monospace")) "monospace" else "sans-serif", if (style.bold) Typeface.BOLD else Typeface.NORMAL)
                    textAlign = style.anchor
                }
                canvas.drawText(e.textContent, e.number("x"), e.number("y"), paint)
            }
            else -> shape(e)?.let { path ->
                val bounds = RectF()
                path.computeBounds(bounds, true)
                if (style.fill != "none") canvas.drawPath(path, paint(style.fill, bounds, style.fillOpacity))
                if (style.stroke != "none") canvas.drawPath(path, paint(style.stroke, bounds, style.strokeOpacity).apply {
                    this.style = Paint.Style.STROKE
                    strokeWidth = style.strokeWidth
                    strokeCap = style.cap
                    strokeJoin = style.join
                    val dash = e.getAttribute("stroke-dasharray")
                    if (dash.isNotEmpty()) pathEffect = android.graphics.DashPathEffect(numbers(dash), 0f)
                })
            }
        }
        if (alphaSave != null) canvas.restoreToCount(alphaSave)
        canvas.restoreToCount(save)
    }

    private fun shape(e: Element): Path? = when (e.localName) {
        "path" -> PathParser.createPathFromPathData(e.getAttribute("d"))
        "circle" -> Path().apply { addCircle(e.number("cx"), e.number("cy"), e.number("r"), Path.Direction.CW) }
        "ellipse" -> Path().apply {
            val x = e.number("cx"); val y = e.number("cy"); val rx = e.number("rx"); val ry = e.number("ry")
            addOval(RectF(x - rx, y - ry, x + rx, y + ry), Path.Direction.CW)
        }
        "rect" -> Path().apply {
            val x = e.number("x"); val y = e.number("y")
            val rx = e.number("rx", e.number("ry")); val ry = e.number("ry", rx)
            addRoundRect(RectF(x, y, x + e.number("width"), y + e.number("height")), rx, ry, Path.Direction.CW)
        }
        "line" -> Path().apply { moveTo(e.number("x1"), e.number("y1")); lineTo(e.number("x2"), e.number("y2")) }
        else -> null
    }

    private fun paint(value: String, bounds: RectF, opacity: Float): Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        if (value.startsWith("url(#")) {
            val gradient = definitions.getValue(value.removePrefix("url(#").removeSuffix(")"))
            val stops = gradient.children()
            val colors = stops.map { stop ->
                val c = svgColor(stop.getAttribute("stop-color"))
                Color.argb((Color.alpha(c) * stop.number("stop-opacity", 1f)).toInt(), Color.red(c), Color.green(c), Color.blue(c))
            }.toIntArray()
            val offsets = stops.map { it.number("offset") }.toFloatArray()
            shader = if (gradient.localName == "radialGradient") {
                RadialGradient(gradient.number("cx", .5f), gradient.number("cy", .5f), gradient.number("r", .5f), colors, offsets, Shader.TileMode.CLAMP)
            } else {
                LinearGradient(gradient.number("x1"), gradient.number("y1"), gradient.number("x2", 1f), gradient.number("y2"), colors, offsets, Shader.TileMode.CLAMP)
            }
            shader!!.setLocalMatrix(Matrix().apply {
                setScale(bounds.width().coerceAtLeast(.001f), bounds.height().coerceAtLeast(.001f))
                postTranslate(bounds.left, bounds.top)
            })
        } else color = svgColor(value)
        alpha = (alpha * opacity).toInt().coerceIn(0, 255)
    }

    private fun applyTransform(canvas: Canvas, value: String) {
        Regex("([a-zA-Z]+)\\(([^)]*)\\)").findAll(value).forEach { match ->
            val n = numbers(match.groupValues[2])
            when (match.groupValues[1]) {
                "translate" -> canvas.translate(n[0], n.getOrElse(1) { 0f })
                "scale" -> canvas.scale(n[0], n.getOrElse(1) { n[0] })
                "rotate" -> canvas.rotate(n[0], n.getOrElse(1) { 0f }, n.getOrElse(2) { 0f })
            }
        }
    }

    private fun svgColor(value: String): Int {
        val full = if (value.startsWith("#") && value.length == 4) "#" + value.drop(1).flatMap { listOf(it, it) }.joinToString("") else value
        return Color.parseColor(full)
    }

    private fun numbers(value: String): FloatArray = Regex("[-+]?(?:\\d*\\.\\d+|\\d+)(?:[eE][-+]?\\d+)?").findAll(value).map { it.value.toFloat() }.toList().toFloatArray()
}

private fun Element.children(): List<Element> = (0 until childNodes.length).mapNotNull { childNodes.item(it) as? Element }
private fun Element.attr(name: String, fallback: String): String = if (hasAttribute(name)) getAttribute(name) else fallback
private fun Element.number(name: String, fallback: Float = 0f): Float {
    val value = getAttribute(name)
    return if (value.endsWith("%")) (value.dropLast(1).toFloatOrNull() ?: (fallback * 100)) / 100f else value.toFloatOrNull() ?: fallback
}
