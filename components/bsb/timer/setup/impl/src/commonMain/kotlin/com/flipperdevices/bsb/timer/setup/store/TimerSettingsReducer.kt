package com.flipperdevices.bsb.timer.setup.store

import com.flipperdevices.bsb.dao.model.TimerDuration
import com.flipperdevices.bsb.dao.model.TimerSettings
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours

object TimerSettingsReducer {

    private fun TimerSettings.onRestChanged(message: Message.Interval.RestChanged): TimerSettings {
        val totalTime = when (val localTotalTime = totalTime) {
            is TimerDuration.Finite -> localTotalTime.instance
            TimerDuration.Infinite -> return this
        }
        val newState = copy(
            intervalsSettings = intervalsSettings.copy(rest = message.value)
        )
        val iterationDuration = newState
            .intervalsSettings
            .work
            .plus(newState.intervalsSettings.rest)
        return if (iterationDuration > totalTime) {
            newState.copy(
                intervalsSettings = newState.intervalsSettings.copy(
                    rest = totalTime.minus(newState.intervalsSettings.work)
                )
            )
        } else {
            newState
        }
    }
    private fun TimerSettings.onLongRestChanged(message: Message.Interval.LongRestChanged): TimerSettings {
        return copy(
            intervalsSettings = intervalsSettings
                .copy(longRest = message.value)
        )
    }
    private fun TimerSettings.onWorkChanged(message: Message.Interval.WorkChanged): TimerSettings {
        return copy(
            intervalsSettings = intervalsSettings
                .copy(work = message.value)
        )
    }
    private fun TimerSettings.onTotalTimeChanged(message: Message.TotalTimeChanged): TimerSettings {
        val newState = copy(totalTime = TimerDuration(message.value))
        return when (val localTotalTime = newState.totalTime) {
            is TimerDuration.Finite -> {
                if (localTotalTime.instance < 1.hours) {
                    newState.copy(
                        intervalsSettings = newState
                            .intervalsSettings
                            .copy(isEnabled = false)
                    )
                } else {
                    newState
                }
            }

            TimerDuration.Infinite -> {
                newState.copy(
                    intervalsSettings = newState
                        .intervalsSettings
                        .copy(isEnabled = false)
                )
            }
        }
    }

    @Suppress("LongMethod")
    fun TimerSettings.reduce(message: Message): TimerSettings {
        return when (message) {
            is Message.Interval.LongRestChanged -> {
                onLongRestChanged(message)
            }

            is Message.Interval.RestChanged -> {
                onRestChanged(message)
            }

            is Message.Interval.WorkChanged -> {
                onWorkChanged(message)
            }

            is Message.TotalTimeChanged -> {
                onTotalTimeChanged(message)
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
