package com.tamagotchi.code.ui.components

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Shader

/** Garments sit over the torso, behind the face and the author's front props. */
internal fun drawCatClothing(canvas: Canvas, id: String?, a: CatAnchors) {
    val color = when (id) {
        "scarf" -> Color.rgb(232, 121, 144)
        "dev_jacket" -> Color.rgb(37, 108, 129)
        "js_jacket" -> Color.rgb(232, 189, 53)
        "php_robe" -> Color.rgb(133, 116, 194)
        else -> return
    }
    val half = a.bodyWidth / 2 - 5f
    val top = a.bodyTop + 4f
    val bottom = -10f
    val fill = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        shader = LinearGradient(0f, top, half, bottom, color, Color.rgb(Color.red(color) * 2 / 3, Color.green(color) * 2 / 3, Color.blue(color) * 2 / 3), Shader.TileMode.CLAMP)
    }
    val edge = Paint(Paint.ANTI_ALIAS_FLAG).apply { this.color = Color.rgb(91, 58, 41); style = Paint.Style.STROKE; strokeWidth = 2f; strokeJoin = Paint.Join.ROUND }
    if (id == "scarf") {
        val scarf = RectF(-half, top, half, top + 10f)
        canvas.drawRoundRect(scarf, 4f, 4f, fill); canvas.drawRoundRect(scarf, 4f, 4f, edge)
        canvas.drawRoundRect(RectF(half - 13, top + 6, half - 3, bottom), 3f, 3f, fill)
    } else {
        for (side in listOf(-1f, 1f)) {
            val panel = Path().apply {
                moveTo(side * half * .6f, top); lineTo(side * 5f, top)
                lineTo(side * 11f, top + (bottom - top) * .35f); lineTo(side * 6f, bottom)
                lineTo(side * half, bottom); quadTo(side * (half + 7), top + 20, side * half * .6f, top); close()
            }
            canvas.drawPath(panel, fill); canvas.drawPath(panel, edge)
        }
        if (id == "js_jacket" || id == "php_robe") {
            canvas.drawRoundRect(RectF(-half + 7, top + 14, -half + 19, top + 23), 2f, 2f, Paint(Paint.ANTI_ALIAS_FLAG).apply { this.color = Color.rgb(30, 42, 50) })
        }
    }
}

internal fun drawCatHat(canvas: Canvas, id: String?, a: CatAnchors) {
    val color = when (id) {
        "beanie" -> Color.rgb(232, 121, 144)
        "top_hat" -> Color.rgb(39, 56, 76)
        "kotlin_beret" -> Color.rgb(159, 117, 234)
        "python_hood" -> Color.rgb(54, 117, 167)
        else -> return
    }
    // The ears remain exposed; headwear is placed on the crown between them.
    val w = a.headRadius * .48f
    val y = a.headTop + 18f
    val shape = Path().apply {
        when (id) {
            "top_hat" -> { moveTo(-w, y); lineTo(-w * .85f, y - 28); lineTo(w * .85f, y - 28); lineTo(w, y) }
            "kotlin_beret" -> { moveTo(-w, y); cubicTo(-w * 1.4f, y - 22, w * .8f, y - 26, w * 1.2f, y - 7); lineTo(w, y) }
            else -> { moveTo(-w, y); cubicTo(-w, y - 32, w, y - 32, w, y) }
        }
        close()
    }
    val fill = Paint(Paint.ANTI_ALIAS_FLAG).apply { this.color = color }
    val edge = Paint(Paint.ANTI_ALIAS_FLAG).apply { this.color = Color.rgb(91, 58, 41); style = Paint.Style.STROKE; strokeWidth = 2f }
    canvas.drawPath(shape, fill); canvas.drawPath(shape, edge)
    canvas.drawRoundRect(RectF(-w - 3, y - 3, w + 3, y + 4), 3f, 3f, fill)
    when (id) {
        "beanie" -> canvas.drawCircle(0f, y - 24, 5f, fill)
        "top_hat" -> canvas.drawRect(RectF(-w * .88f, y - 9, w * .88f, y - 4), Paint(Paint.ANTI_ALIAS_FLAG).apply { this.color = Color.rgb(232, 189, 53) })
        "kotlin_beret" -> canvas.drawPath(Path().apply { moveTo(-6f, y - 16); lineTo(5f, y - 16); lineTo(-6f, y - 5); close() }, Paint(Paint.ANTI_ALIAS_FLAG).apply { this.color = Color.rgb(255, 175, 99) })
        "python_hood" -> canvas.drawRoundRect(RectF(-7f, y - 18, 7f, y - 7), 4f, 4f, Paint(Paint.ANTI_ALIAS_FLAG).apply { this.color = Color.rgb(255, 215, 107) })
    }
}
