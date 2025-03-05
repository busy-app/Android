package com.flipperdevices.bsb.sound.api

import android.content.Context
import android.media.MediaPlayer
import androidx.annotation.RawRes
import me.tatarka.inject.annotations.Inject
import com.flipperdevices.bsb.sound.impl.R

enum class Sound(@RawRes val resId: Int) {
    REST_COUNTDOWN(R.raw.rest_countdown),
    REST_FINISHED(R.raw.rest_finished),
    WORK_COUNTDOWN(R.raw.work_countdown),
    WORK_FINISHED(R.raw.work_finished)
}

@Inject
class SoundPlayHelper(
    private val context: Context
) {
    fun play(sound: Sound) {
        val player = MediaPlayer.create(context, sound.resId)
        player.setOnCompletionListener {
            player.release()
        }
        player.isLooping = false
        player.start()
    }
}