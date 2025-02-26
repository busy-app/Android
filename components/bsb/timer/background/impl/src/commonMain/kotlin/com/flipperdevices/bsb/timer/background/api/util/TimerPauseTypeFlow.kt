package com.flipperdevices.bsb.timer.background.api.util

import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.background.model.PauseType
import com.flipperdevices.bsb.timer.background.model.isLastIteration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

class TimerPauseTypeFlow(
    timerApi: TimerApi
) : Flow<PauseType> by timerApi
    .getState()
    .filterIsInstance<ControlledTimerState.Running>()
    .distinctUntilChangedBy({ it.timeLeft.inWholeSeconds })
    .map(
        transform = { state ->
            if (state.timeLeft.inWholeSeconds != 0L) {
                null
            } else {
                when (state) {
                    is ControlledTimerState.Running.LongRest -> {
                        if (state.timerSettings.intervalsSettings.autoStartWork) {
                            null
                        } else if (state.isLastIteration) {
                            null
                        } else {
                            PauseType.AFTER_REST
                        }
                    }

                    is ControlledTimerState.Running.Rest -> {
                        if (state.timerSettings.intervalsSettings.autoStartWork) {
                            null
                        } else {
                            PauseType.AFTER_REST
                        }
                    }

                    is ControlledTimerState.Running.Work -> {
                        if (state.timerSettings.intervalsSettings.autoStartRest) {
                            null
                        } else {
                            PauseType.AFTER_WORK
                        }
                    }
                }
            }
        }
    ).filterNotNull()
