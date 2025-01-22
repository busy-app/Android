package com.flipperdevices.bsb.appblocker.listener

import com.flipperdevices.bsb.appblocker.api.AppBlockerApi
import com.flipperdevices.bsb.appblocker.api.FamilyControlApi
import com.flipperdevices.bsb.timer.background.api.TimerStateListener
import com.flipperdevices.core.di.AppGraph
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
    override fun onTimerStart() {
        if (appBlockerApi.isAppBlockerSupportActive()) {
            familyControlApi.enableFamilyControls()
        }
    }

    override fun onTimerStop() {
        familyControlApi.disableFamilyControls()
    }
}
