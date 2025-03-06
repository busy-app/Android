package com.flipperdevices.bsb.timer.main.api

import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.wear.messenger.di.WearMessengerModule
import com.flipperdevices.bsb.wear.messenger.model.TimerTimestampMessage
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.di.ComponentHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn


@Inject
@SingleIn(AppGraph::class)
@ContributesBinding(AppGraph::class, MyService::class)
class AndroidMyService(
    private val timerApi: TimerApi,
    private val wearMessengerModule: WearMessengerModule
) : MyService {

    override fun start(scope: CoroutineScope) {
        timerApi
            .getTimestampState()
            .onEach {
                wearMessengerModule.wearMessageProducer.produce(
                    TimerTimestampMessage,
                    timerApi.getTimestampState().first()
                )
            }.launchIn(scope)
    }
}