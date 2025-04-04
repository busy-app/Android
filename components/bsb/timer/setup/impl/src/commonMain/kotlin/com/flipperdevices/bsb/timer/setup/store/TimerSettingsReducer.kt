package com.flipperdevices.bsb.timer.setup.store

import com.flipperdevices.bsb.dao.model.TimerDuration
import com.flipperdevices.bsb.dao.model.TimerSettings
import kotlin.time.Duration

/**
 * Some timer configs should be changed when we are changing others
 * So the [TimerSettings] should be changed via [TimerSettingsReducer]
 */
object TimerSettingsReducer {
    private fun TimerSettings.onRestChanged(message: Message.Interval.RestChanged): TimerSettings {
        return copy(
            intervalsSettings = intervalsSettings
                .copy(rest = message.value)
        )
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
        val totalTime = when (message.value) {
            Duration.ZERO -> TimerDuration.Infinite
            else -> TimerDuration.Finite(message.value)
        }
        return copy(totalTime = totalTime)
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
