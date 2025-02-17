package com.flipperdevices.bsb.timer.setup.store

import com.flipperdevices.bsb.preference.model.TimerSettings
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours

object TimerSettingsReducer {

    fun TimerSettings.reduce(message: Message): TimerSettings {
        return when (message) {
            is Message.Interval.LongRestChanged -> {
                copy(
                    intervalsSettings = intervalsSettings
                        .copy(longRest = message.value)
                )
            }

            is Message.Interval.RestChanged -> {
                copy(
                    intervalsSettings = intervalsSettings
                        .copy(rest = message.value)
                ).let { newState ->
                    val iterationDuration = newState
                        .intervalsSettings
                        .work
                        .plus(newState.intervalsSettings.rest)
                    if (iterationDuration > newState.totalTime) {
                        newState.copy(
                            intervalsSettings = newState.intervalsSettings.copy(
                                rest = newState.totalTime.minus(newState.intervalsSettings.work)
                            )
                        )
                    } else {
                        newState
                    }
                }
            }

            is Message.Interval.WorkChanged -> {
                copy(
                    intervalsSettings = intervalsSettings
                        .copy(work = message.value)
                )
            }

            is Message.TotalTimeChanged -> {
                copy(totalTime = message.value).let { newState ->
                    if (newState.totalTime < 1.hours) {
                        newState.copy(
                            intervalsSettings = newState
                                .intervalsSettings
                                .copy(isEnabled = false)
                        )
                    } else {
                        newState
                    }
                }
            }
        }
    }

    sealed interface Message {
        class TotalTimeChanged(val value: Duration) : Message
        sealed interface Interval : Message {
            class WorkChanged(val value: Duration) : Interval
            class RestChanged(val value: Duration) : Interval
            class LongRestChanged(val value: Duration) : Interval
        }
    }
}
