package com.tamagotchi.code.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.tamagotchi.code.R
import com.tamagotchi.code.data.PetOutfit
import com.tamagotchi.code.ui.theme.LocalAppTheme
import com.tamagotchi.code.ui.theme.LocalReduceMotion
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharedFlow
import kotlin.math.sin

internal enum class PetMood { HAPPY, SLEEPING, STUDYING, SICK, HUNGRY, SAD, EXCITED, DEAD }

internal fun codeyMood(status: String, isDead: Boolean = false): PetMood =
    if (isDead) PetMood.DEAD else PetMood.entries.firstOrNull { it.name == status } ?: PetMood.HAPPY

/** The same SVG-derived cat is used in every screen and in the launcher widget. */
@Composable
fun CodeySprite(
    status: String,
    level: Int = 1,
    isDead: Boolean = false,
    celebrationTrigger: SharedFlow<Unit>? = null,
    learningEventTrigger: SharedFlow<String>? = null,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    animationEnabled: Boolean = true,
    outfit: PetOutfit = PetOutfit(),
    pixelModeOverride: Boolean? = null
) {
    val mood = codeyMood(status, isDead)
    val stage = PetEvolutionStage.fromLevel(level)
    val animate = animationEnabled && !LocalReduceMotion.current && mood != PetMood.DEAD
    val pixelMode = pixelModeOverride ?: (LocalAppTheme.current.name == "Retro Pixel")
    var celebrating by remember { mutableStateOf(false) }
    var failed by remember { mutableStateOf(false) }
    var touched by remember { mutableStateOf(false) }
    val evolution = remember { Animatable(0f) }
    var previousStage by remember { mutableStateOf(stage) }

    LaunchedEffect(stage, animate) {
        if (stage != previousStage && animate) {
            evolution.snapTo(1f)
            evolution.animateTo(0f, tween(1000))
        } else evolution.snapTo(0f)
        previousStage = stage
    }
    LaunchedEffect(celebrationTrigger, animate) {
        celebrating = false
        celebrationTrigger?.collect {
            if (animate) { celebrating = true; delay(650); celebrating = false }
        }
    }
    LaunchedEffect(learningEventTrigger, animate) {
        failed = false
        learningEventTrigger?.collect { event ->
            if (animate) {
                if (event == "SUCCESS") celebrating = true else if (event == "FAILURE") failed = true
                delay(400)
                celebrating = false
                failed = false
            }
        }
    }
    LaunchedEffect(touched, animate) {
        if (touched) { delay(650); touched = false }
    }
    val description = stringResource(R.string.cd_widget_pet)
    // Do not keep an infinite clock invalidating frames when motion is disabled.
    val clock = if (animate) catAnimationClock() else null
    Canvas(modifier = modifier.size(170.dp).semantics { contentDescription = description }
        .then(if (onClick != null) Modifier.clickable {
            if (animate) touched = true
            onClick()
        } else Modifier)) {
        // Read the clock in the draw phase: animate without recomposing the UI.
        val ticks = clock?.value ?: 0f
        val effectiveMood = if ((celebrating || touched) && animate) PetMood.EXCITED else mood
        rotate(if (failed && animate) sin(ticks * .8f) * 5f else 0f) {
            if (pixelMode) drawCodeyPixelFrame(stage, effectiveMood, ticks, outfit, evolution.value)
            else drawCodeyFrame(stage, effectiveMood, ticks, evolution.value, outfit)
        }
    }
}

@Composable
private fun catAnimationClock(): State<Float> {
    val clock = rememberInfiniteTransition(label = "ProgrammerCatClock")
    return clock.animateFloat(0f, CAT_ANIMATION_PERIOD,
        infiniteRepeatable(tween((CAT_ANIMATION_PERIOD * 1000f / 60f).toInt(), easing = LinearEasing), RepeatMode.Restart), label = "catTicks")
}

internal fun DrawScope.drawCodeyFrame(
    stage: PetEvolutionStage, mood: PetMood, time: Float,
    evolutionProgress: Float = 0f, outfit: PetOutfit = PetOutfit()
) {
    drawIntoCanvas { canvas ->
        ProgrammerCatRenderer.draw(canvas.nativeCanvas, size.width, size.height, stage, mood, time, evolutionProgress, outfit)
    }
}
