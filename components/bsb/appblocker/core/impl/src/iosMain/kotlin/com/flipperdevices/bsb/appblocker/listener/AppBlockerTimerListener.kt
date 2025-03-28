package com.flipperdevices.bsb.appblocker.listener

import com.flipperdevices.bsb.appblocker.api.AppBlockerApi
import com.flipperdevices.bsb.appblocker.api.FamilyControlApi
import com.flipperdevices.bsb.dao.model.TimerSettings
import com.flipperdevices.bsb.timer.background.api.TimerStateListener
import com.flipperdevices.core.di.AppGraph
import kotlinx.coroutines.flow.first
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppGraph::class)
@ContributesBinding(AppGraph::class, TimerStateListener::class, multibinding = true)
class AppBlockerTimerListener(
    private val appBlockerApi: AppBlockerApi,
    private val familyControlApi: FamilyControlApi
) : TimerStateListener {
    override suspend fun onTimerStart(timerSettings: TimerSettings) {
        if (appBlockerApi.isAppBlockerSupportActive().first()) {
            familyControlApi.enableFamilyControls()
        }
    }

    override suspend fun onTimerStop() {
        familyControlApi.disableFamilyControls()
    }
}
