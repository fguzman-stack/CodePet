package com.tamagotchi.code.util

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MusicManager {
    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying = false
    private var volume = 0.3f

    fun play(context: Context, resId: Int, loop: Boolean = true) {
        stop()
        try {
            val mp = MediaPlayer.create(context, resId)
            mp.setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            mp.isLooping = loop
            mp.setVolume(volume, volume)
            mp.start()
            isPlaying = true
            mp.setOnCompletionListener { isPlaying = false }
            mediaPlayer = mp
        } catch (_: Exception) {}
    }

    fun playRaw(context: Context, rawResId: Int, loop: Boolean = true) {
        play(context, rawResId, loop)
    }

    fun pause() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                isPlaying = false
            }
        }
    }

    fun resume() {
        mediaPlayer?.let {
            if (!it.isPlaying) {
                it.start()
                isPlaying = true
            }
        }
    }

    fun stop() {
        mediaPlayer?.let {
            try {
                it.stop()
                it.release()
            } catch (_: Exception) {}
            mediaPlayer = null
            isPlaying = false
        }
    }

    fun setVolume(vol: Float) {
        volume = vol.coerceIn(0f, 1f)
        mediaPlayer?.setVolume(volume, volume)
    }

    val isActive: Boolean get() = isPlaying

    fun release() {
        stop()
    }
}
