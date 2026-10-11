package com.tamagotchi.code.util

import android.media.AudioManager
import android.media.ToneGenerator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SoundManager(private val isEnabled: () -> Boolean = { true }) {
    private var toneGen: ToneGenerator? = null
    private val soundScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    init {
        try {
            toneGen = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
        } catch (e: Exception) {
            // Ignored
        }
    }

    fun playSuccess() {
        playTone(ToneGenerator.TONE_PROP_ACK, 100)
    }

    fun playError() {
        playTone(ToneGenerator.TONE_PROP_BEEP2, 150)
    }

    fun playLevelUp() {
        soundScope.launch {
            playTone(ToneGenerator.TONE_PROP_BEEP, 100)
            delay(150)
            playTone(ToneGenerator.TONE_PROP_BEEP, 100)
            delay(150)
            playTone(ToneGenerator.TONE_PROP_ACK, 200)
        }
    }

    fun playClick() {
        playTone(ToneGenerator.TONE_DTMF_A, 50)
    }

    fun playBuy() {
        playTone(ToneGenerator.TONE_PROP_BEEP, 100)
    }
    
    fun playSleep() {
        soundScope.launch {
            playTone(ToneGenerator.TONE_CDMA_SOFT_ERROR_LITE, 100)
            delay(1000)
            playTone(ToneGenerator.TONE_CDMA_SOFT_ERROR_LITE, 100)
        }
    }

    @Synchronized
    private fun playTone(tone: Int, duration: Int) {
        if (isEnabled()) toneGen?.startTone(tone, duration)
    }

    @Synchronized
    fun release() {
        soundScope.cancel()
        toneGen?.release()
        toneGen = null
    }
}
