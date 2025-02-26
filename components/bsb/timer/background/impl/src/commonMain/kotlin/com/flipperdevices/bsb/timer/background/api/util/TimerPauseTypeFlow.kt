package com.flipperdevices.bsb.timer.background.api.util

import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.background.model.PauseType
import com.flipperdevices.bsb.timer.background.model.isLastIteration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map

private enum class EventType {
    NONE, PAUSE_AFTER_WORK, PAUSE_AFTER_REST
}

class TimerPauseTypeFlow(
    timerApi: TimerApi
) : Flow<PauseType> by timerApi
    .getState()
    .filterIsInstance<ControlledTimerState.Running>()
    .distinctUntilChangedBy({ it.timeLeft.inWholeSeconds })
    .map(
        transform = { state ->
            if (state.timeLeft.inWholeSeconds != 0L) {
                EventType.NONE
            } else {
                when (state) {
                    is ControlledTimerState.Running.LongRest -> {
                        if (state.timerSettings.intervalsSettings.autoStartWork) {
                            EventType.NONE
                        } else if (state.isLastIteration) {
                            EventType.NONE
                        } else {
                            EventType.PAUSE_AFTER_REST
                        }
                    }

                    is ControlledTimerState.Running.Rest -> {
                        if (state.timerSettings.intervalsSettings.autoStartWork) {
                            EventType.NONE
                        } else {
                            EventType.PAUSE_AFTER_REST
                        }
                    }

                    is ControlledTimerState.Running.Work -> {
                        if (state.timerSettings.intervalsSettings.autoStartRest) {
                            EventType.NONE
                        } else {
                            EventType.PAUSE_AFTER_WORK
                        }
                    }
                }
            }
        }
    )
    .map(
        transform = { eventType ->
            when (eventType) {
                EventType.PAUSE_AFTER_WORK -> PauseType.AFTER_WORK
                EventType.PAUSE_AFTER_REST -> PauseType.AFTER_REST
                EventType.NONE -> return@onEach
            }
        }
    )
