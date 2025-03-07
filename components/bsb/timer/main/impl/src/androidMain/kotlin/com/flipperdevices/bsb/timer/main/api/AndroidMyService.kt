package com.flipperdevices.bsb.timer.main.api

import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.wear.messenger.model.TimerTimestampMessage
import com.flipperdevices.bsb.wear.messenger.producer.WearMessageProducer
import com.flipperdevices.bsb.wear.messenger.producer.produce
import com.flipperdevices.core.di.AppGraph
import kotlinx.coroutines.CoroutineScope
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
    private val wearMessageProducer: WearMessageProducer
) : MyService {

    override fun start(scope: CoroutineScope) {
        timerApi
            .getTimestampState()
            .onEach {
                val message = TimerTimestampMessage(timerApi.getTimestampState().first())
                wearMessageProducer.produce(message)
            }.launchIn(scope)
    }
}