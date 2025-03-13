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
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

enum class Sound(@RawRes val resId: Int) {
    REST_COUNTDOWN(R.raw.rest_countdown_single),
    REST_FINISHED(R.raw.rest_finished),
    WORK_COUNTDOWN(R.raw.work_countdown_single),
    WORK_FINISHED(R.raw.work_finished)
}

@Inject
@SingleIn(AppGraph::class)
class SoundPlayHelper(
    private val context: Context,
    private val scope: CoroutineScope
) : LogTagProvider {
    override val TAG = "SoundPlayHelper"

    private var currentPlayer: MediaPlayer? = null
    private val mutex = Mutex()

    suspend fun play(sound: Sound) = withContext(Dispatchers.Main) {
        val player = MediaPlayer.create(context, sound.resId)
        player.isLooping = false
        mutex.withLock {
            releasePlayerUnsafe()
            info { "Start playing for $sound" }
            player.setOnCompletionListener {
                info { "Release player for sound $sound" }
                scope.launch(Dispatchers.Main) {
                    mutex.withLock {
                        if (currentPlayer == player) {
                            releasePlayerUnsafe()
                        }
                    }
                }
            }
            player.start()
            currentPlayer = player
        }
    }

    private fun releasePlayerUnsafe() {
        val localPlayer = currentPlayer
        if (localPlayer != null) {
            info { "Stop playing for $localPlayer" }
            localPlayer.stop()
            localPlayer.release()
            currentPlayer = null
        }
    }
}
