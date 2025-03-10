package com.flipperdevices.bsbwearable.timer

import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.timer.background.flow.TickFlow
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.background.model.TimerTimestamp
import com.flipperdevices.bsb.timer.background.util.toState
import com.flipperdevices.bsb.wear.messenger.model.TimerTimestampMessage
import com.flipperdevices.bsb.wear.messenger.producer.WearMessageProducer
import com.flipperdevices.bsb.wear.messenger.producer.produce
import com.flipperdevices.core.di.AppGraph
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@ContributesBinding(AppGraph::class, TimerApi::class)
@SingleIn(AppGraph::class)
class WearTimerApi(
    private val scope: CoroutineScope,
    private val wearMessageProducer: WearMessageProducer
) : TimerApi {
    private val timerTimestampStateFlow = MutableStateFlow<TimerTimestamp?>(null)

    override fun setTimestampState(state: TimerTimestamp?, broadcast: Boolean) {
        timerTimestampStateFlow.update { oldState ->
            val newState = when {
                oldState == null || state == null -> state
                state.lastSync > oldState.lastSync -> state
                else -> oldState
            }
            if (broadcast) {
                scope.launch { wearMessageProducer.produce(TimerTimestampMessage(newState)) }
            }
            newState
        }
    }

    override fun getTimestampState(): StateFlow<TimerTimestamp?> {
        return timerTimestampStateFlow.asStateFlow()
    }

    private val state = combine(
        flow = timerTimestampStateFlow,
        flow2 = TickFlow(),
        transform = { timerTimestamp, tick -> timerTimestamp.toState() }
    ).stateIn(scope, SharingStarted.Companion.Eagerly, ControlledTimerState.NotStarted)

    override fun getState(): StateFlow<ControlledTimerState> = state
}
