package com.tamagotchi.code.util

import android.media.AudioManager
import android.media.ToneGenerator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SoundManager {
    private var toneGen: ToneGenerator? = null

    init {
        try {
            toneGen = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
        } catch (e: Exception) {
            // Ignored
        }
    }

    fun playSuccess() {
        toneGen?.startTone(ToneGenerator.TONE_PROP_ACK, 100)
    }

    fun playError() {
        toneGen?.startTone(ToneGenerator.TONE_PROP_BEEP2, 150)
    }

    fun playLevelUp() {
        CoroutineScope(Dispatchers.Default).launch {
            toneGen?.startTone(ToneGenerator.TONE_PROP_BEEP, 100)
            delay(150)
            toneGen?.startTone(ToneGenerator.TONE_PROP_BEEP, 100)
            delay(150)
            toneGen?.startTone(ToneGenerator.TONE_PROP_ACK, 200)
        }
    }

    fun playClick() {
        toneGen?.startTone(ToneGenerator.TONE_DTMF_A, 50)
    }

    fun playBuy() {
        toneGen?.startTone(ToneGenerator.TONE_PROP_BEEP, 100)
    }
    
    fun playSleep() {
        CoroutineScope(Dispatchers.Default).launch {
            toneGen?.startTone(ToneGenerator.TONE_CDMA_SOFT_ERROR_LITE, 100)
            delay(1000)
            toneGen?.startTone(ToneGenerator.TONE_CDMA_SOFT_ERROR_LITE, 100)
        }
    }

    fun release() {
        toneGen?.release()
        toneGen = null
    }
}
