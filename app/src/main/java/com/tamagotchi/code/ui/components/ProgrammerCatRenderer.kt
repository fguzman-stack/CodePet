package com.tamagotchi.code.ui.components

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import com.tamagotchi.code.data.PetOutfit
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

internal const val CAT_ANIMATION_PERIOD = 7200f

internal data class CatAnchors(
    val eyeX: Float, val eyeY: Float, val eyeRadius: Float, val mouthY: Float,
    val headTop: Float, val headRadius: Float, val bodyWidth: Float, val bodyTop: Float,
    val tailX: Float, val tailY: Float
)

/** Source-space coordinates: (0, 0) is the cat's feet, not a card corner. */
internal val catAnchors = listOf(
    CatAnchors(27f, -90f, 12f, -65f, -158f, 60f, 64f, -48f, 30f, -18f),
    CatAnchors(22f, -116f, 9f, -90f, -177f, 52f, 76f, -83f, 34f, -20f),
    CatAnchors(21.5f, -137f, 7.5f, -113f, -197f, 48f, 84f, -100f, 40f, -16f),
    CatAnchors(21f, -120f, 8f, -92f, -176f, 50f, 80f, -87f, 38f, -12f),
    CatAnchors(25f, -122f, 8.5f, -96f, -170f, 52f, 92f, -88f, 40f, -10f)
)

internal object ProgrammerCatRenderer {
    private val artwork by lazy { ProgrammerCatArt.phases.map { CatSvgRenderer(it).layers } }
    private val ink = Color.rgb(42, 27, 20)
    private val gold = Color.rgb(242, 193, 78)
    private val deadFilter = ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0f) })

    private fun wave(time: Float, period: Float): Float = sin(time * (2f * PI.toFloat() / period))

    fun prewarm() {
        artwork.size
    }

    fun draw(canvas: Canvas, width: Float, height: Float, stage: PetEvolutionStage, mood: PetMood,
             time: Float, evolution: Float, outfit: PetOutfit) {
        val t = if (mood == PetMood.DEAD) 0f else time
        val phase = stage.artIndex
        val anchors = catAnchors[phase]
        val fit = minOf(width / 240f, height / 280f)
        val save = canvas.save()
        canvas.translate((width - 240f * fit) / 2f, (height - 280f * fit) / 2f)
        canvas.scale(fit, fit)
        canvas.translate(120f, 240f)
        val bounce = when (mood) {
            PetMood.EXCITED -> -6f * wave(t, 90f)
            PetMood.SLEEPING -> wave(t, 360f)
            PetMood.SAD, PetMood.SICK -> wave(t, 360f) * 1.2f
            else -> -2f * wave(t, 180f)
        }
        val blink = t % 240f >= 230f
        val changeEyes = blink || mood != PetMood.HAPPY
        var drewEyes = false
        val deadLayer = if (mood == PetMood.DEAD) canvas.saveLayer(null, Paint().apply { colorFilter = deadFilter }) else null

        for (layer in artwork[phase]) {
            val layerSave = canvas.save()
            if (layer.part != "shadow") {
                canvas.translate(0f, bounce)
                canvas.scale(1f, 1f + wave(t, 180f) * .005f, 0f, 0f)
                if (mood == PetMood.DEAD) canvas.rotate(8f, 0f, -75f)
            }
            if (layer.clothingBefore) drawCatClothing(canvas, outfit.clothing, anchors)
            val head = when (layer.part) {
                "head", "eye", "eye_shine", "mouth", "default_hat" -> true
                else -> false
            }
            if (head) canvas.rotate(wave(t, 360f) * (if (mood == PetMood.SLEEPING) 1f else .7f), 0f, anchors.eyeY)
            when (layer.part) {
                "tail" -> canvas.rotate(wave(t, 240f) * (if (mood == PetMood.EXCITED) 8f else 3f), anchors.tailX, anchors.tailY)
                "cape" -> canvas.rotate(wave(t, 240f) * 1.2f, 0f, -88f)
                "prop" -> canvas.translate(0f, wave(t, 360f) * .7f)
                "steam" -> canvas.translate(0f, -wave(t, 180f) * 2f)
            }
            when {
                layer.part == "eye" && changeEyes -> {
                    if (!drewEyes) { drawEyes(canvas, anchors, mood, blink); drewEyes = true }
                }
                layer.part == "eye_shine" && changeEyes -> Unit
                layer.part == "mouth" && mood != PetMood.HAPPY && mood != PetMood.STUDYING -> drawMouth(canvas, anchors, mood)
                layer.part == "default_hat" && outfit.hat != null -> Unit
                layer.part == "cursor" && t % 60f > 35f -> Unit
                else -> {
                    val alpha = if (layer.part == "spark") 1f - (1f - cos(t * 2f * PI.toFloat() / 120f)) * .16f else 1f
                    val alphaSave = if (alpha < 1f) canvas.saveLayerAlpha(null, (alpha * 255).toInt()) else null
                    canvas.translate(-120f, -240f)
                    canvas.drawPicture(layer.picture)
                    canvas.translate(120f, 240f)
                    if (alphaSave != null) canvas.restoreToCount(alphaSave)
                }
            }
            if (layer.hatAfter) drawCatHat(canvas, outfit.hat, anchors)
            canvas.restoreToCount(layerSave)
        }
        if (deadLayer != null) canvas.restoreToCount(deadLayer)
        drawMoodEffect(canvas, anchors, mood, t)
        if (evolution > 0f && mood != PetMood.DEAD) drawEvolution(canvas, evolution, t)
        canvas.restoreToCount(save)
    }

    private fun stroke(color: Int = ink, width: Float = 2.5f) = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        this.color = color; this.strokeWidth = width; style = Paint.Style.STROKE; strokeCap = Paint.Cap.ROUND; strokeJoin = Paint.Join.ROUND
    }

    private fun drawEyes(canvas: Canvas, a: CatAnchors, mood: PetMood, blink: Boolean) {
        val pen = stroke(width = if (a.eyeRadius > 10f) 3f else 2.5f)
        for (side in listOf(-1f, 1f)) {
            val x = a.eyeX * side
            val r = a.eyeRadius * .85f
            when {
                mood == PetMood.DEAD -> {
                    canvas.drawLine(x - r * .6f, a.eyeY - r * .6f, x + r * .6f, a.eyeY + r * .6f, pen)
                    canvas.drawLine(x + r * .6f, a.eyeY - r * .6f, x - r * .6f, a.eyeY + r * .6f, pen)
                }
                mood == PetMood.SLEEPING || blink -> canvas.drawPath(Path().apply {
                    moveTo(x - r, a.eyeY); quadTo(x, a.eyeY + r * .6f, x + r, a.eyeY)
                }, pen)
                mood == PetMood.EXCITED -> canvas.drawPath(Path().apply {
                    moveTo(x - r, a.eyeY + 2); quadTo(x, a.eyeY - r, x + r, a.eyeY + 2)
                }, pen)
                mood == PetMood.SAD || mood == PetMood.SICK -> {
                    canvas.drawOval(RectF(x - r * .65f, a.eyeY, x + r * .65f, a.eyeY + r), Paint(Paint.ANTI_ALIAS_FLAG).apply { color = ink })
                    canvas.drawLine(x - r, a.eyeY - 1, x + r, a.eyeY + 1, pen)
                }
                else -> {
                    canvas.drawCircle(x, a.eyeY, a.eyeRadius, Paint(Paint.ANTI_ALIAS_FLAG).apply { color = ink })
                    canvas.drawCircle(x - 2.5f, a.eyeY - 3.5f, a.eyeRadius * .35f, Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.WHITE })
                }
            }
        }
    }

    private fun drawMouth(canvas: Canvas, a: CatAnchors, mood: PetMood) {
        val path = Path()
        val y = a.mouthY
        when (mood) {
            PetMood.HUNGRY -> canvas.drawOval(RectF(-3f, y - 3f, 3f, y + 4f), Paint(Paint.ANTI_ALIAS_FLAG).apply { color = ink })
            PetMood.EXCITED -> canvas.drawPath(path.apply { moveTo(-8f, y - 2); quadTo(0f, y + 9, 8f, y - 2) }, stroke())
            PetMood.SAD, PetMood.SICK -> canvas.drawPath(path.apply { moveTo(-6f, y + 2); quadTo(0f, y - 4, 6f, y + 2) }, stroke())
            else -> canvas.drawLine(-4f, y, 4f, y, stroke())
        }
    }

    private fun drawMoodEffect(canvas: Canvas, a: CatAnchors, mood: PetMood, t: Float) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        when (mood) {
            PetMood.SLEEPING -> {
                paint.color = Color.rgb(111, 177, 255); paint.typeface = android.graphics.Typeface.MONOSPACE; paint.textSize = 13f
                canvas.drawText("z", 67f, a.eyeY - 32f - wave(t, 180f) * 4, paint)
            }
            PetMood.SAD -> {
                paint.color = Color.rgb(111, 177, 255)
                canvas.drawOval(RectF(34f, a.eyeY + 18 + wave(t, 180f) * 3, 39f, a.eyeY + 26 + wave(t, 180f) * 3), paint)
            }
            PetMood.SICK -> {
                paint.color = Color.rgb(229, 83, 75)
                canvas.drawCircle(66f, a.eyeY - 26, 7f, paint)
                canvas.drawLine(62f, a.eyeY - 26, 70f, a.eyeY - 26, stroke(Color.WHITE, 2f))
                canvas.drawLine(66f, a.eyeY - 30, 66f, a.eyeY - 22, stroke(Color.WHITE, 2f))
            }
            PetMood.HUNGRY -> {
                paint.color = Color.rgb(242, 193, 78)
                canvas.drawArc(RectF(54f, -19f, 77f, -3f), 0f, 180f, true, paint)
                canvas.drawLine(53f, -11f, 78f, -11f, stroke())
            }
            PetMood.STUDYING -> {
                paint.color = Color.rgb(63, 163, 126); paint.typeface = android.graphics.Typeface.MONOSPACE; paint.textSize = 12f
                canvas.drawText("</>", -87f, a.eyeY - 28f - wave(t, 180f) * 2, paint)
            }
            PetMood.EXCITED -> {
                paint.color = gold
                for (i in 0..3) {
                    val angle = t * 2f * PI.toFloat() / 180f + i * PI.toFloat() / 2
                    canvas.drawCircle(cos(angle) * 91f, a.eyeY + sin(angle) * 39f, 2.3f, paint)
                }
            }
            else -> Unit
        }
    }

    private fun drawEvolution(canvas: Canvas, progress: Float, t: Float) {
        val radius = 20f + (1f - progress) * 82f
        val paint = stroke(gold, 2.5f).apply { alpha = (progress * 190).toInt() }
        canvas.drawCircle(0f, -110f, radius, paint)
        paint.style = Paint.Style.FILL
        for (i in 0..7) {
            val angle = t * 2f * PI.toFloat() / 180f + i * PI.toFloat() / 4f
            canvas.drawCircle(cos(angle) * radius, -110f + sin(angle) * radius, 3f, paint)
        }
    }
}
