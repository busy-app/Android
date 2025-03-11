package com.flipperdevices.bsb.sound.helper

import android.content.Context
import android.media.MediaPlayer
import androidx.annotation.RawRes
import com.flipperdevices.bsb.sound.impl.R
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.log.info
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

enum class Sound(@RawRes val resId: Int) {
    REST_COUNTDOWN(R.raw.rest_countdown),
    REST_FINISHED(R.raw.rest_finished),
    WORK_COUNTDOWN(R.raw.work_countdown),
    WORK_FINISHED(R.raw.work_finished)
}

@Inject
@SingleIn(AppGraph::class)
class SoundPlayHelper(
    private val context: Context,
    private val scope: CoroutineScope
) : LogTagProvider {
    override val TAG = "SoundPlayHelper"

    private val currentSoundPlayers = mutableListOf<MediaPlayer>()
    private val mutex = Mutex()

    suspend fun play(sound: Sound) = mutex.withLock {
        info { "Start playing for $sound" }
        val player = MediaPlayer.create(context, sound.resId)
        player.setOnCompletionListener {
            info { "Release player for sound $sound" }
            scope.launch(Dispatchers.Main) {
                mutex.withLock {
                    currentSoundPlayers.remove(player)
                }
                player.release()
            }
        }
        player.isLooping = false
        player.start()
        currentSoundPlayers.add(player)
    }
}
