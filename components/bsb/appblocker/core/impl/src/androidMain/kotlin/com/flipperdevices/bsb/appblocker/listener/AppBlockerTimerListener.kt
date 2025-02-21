package com.flipperdevices.bsb.appblocker.listener

import com.flipperdevices.bsb.appblocker.api.AppBlockerApi
import com.flipperdevices.bsb.timer.background.api.TimerStateListener
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.log.info
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppGraph::class)
@ContributesBinding(AppGraph::class, TimerStateListener::class, multibinding = true)
class AppBlockerTimerListener(
    private val appBlockerApi: AppBlockerApi,
    private val looperFactory: () -> UsageStatsLooper
) : TimerStateListener, LogTagProvider {
    override val TAG = "AppBlockerTimer"

    private var looper: UsageStatsLooper? = null

    override fun onTimerStart() {
        if (runBlocking { appBlockerApi.isAppBlockerSupportActive().first() }) {
            info { "Start usage stats looper for app blocker" }
            val nonNullLooper = looper ?: looperFactory().also { looper = it }
            nonNullLooper.startLoop()
        }
    }

    override fun onTimerStop() {
        looper?.stopLoop()
    }
}
