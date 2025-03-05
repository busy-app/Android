package com.flipperdevices.bsb.dnd.listener

import android.app.NotificationManager
import android.content.Context
import com.flipperdevices.bsb.dnd.api.BusyDNDApi
import com.flipperdevices.bsb.preference.model.TimerSettings
import com.flipperdevices.bsb.timer.background.api.TimerStateListener
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.log.info
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(AppGraph::class, TimerStateListener::class, multibinding = true)
class DNDTimerListener(
    context: Context,
    private val dndApi: BusyDNDApi
) : TimerStateListener, LogTagProvider {
    override val TAG = "DNDTimerListener"

    private val notificationManager by lazy { context.getSystemService(NotificationManager::class.java) }

    override fun onTimerStart(timerSettings: TimerSettings) {
        if (dndApi.isDNDSupportActive()) {
            info { "Receive #onTimerStart and turn on DND" }
            notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE)
        }
    }

    override fun onTimerStop() {
        if (dndApi.isDNDSupportActive()) {
            info { "Receive #onTimerStop and turn off DND" }
            notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
        }
    }
}
