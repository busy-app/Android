package com.flipperdevices.bsb.timer.background.api.store

import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import kotlin.time.Duration

object ControlledTimerStateReducer {

    private fun ControlledTimerState.onTimeUpdated(msg: Message.TimeUpdated): ControlledTimerState {
        return when (this) {
            ControlledTimerState.NotStarted,
            ControlledTimerState.Finished -> this

            is ControlledTimerState.Running.LongRest -> {
                this.copy(timeLeft = msg.newTimeLeft)
            }

            is ControlledTimerState.Running.Rest -> {
                this.copy(timeLeft = msg.newTimeLeft)
            }

            is ControlledTimerState.Running.Work -> {
                this.copy(timeLeft = msg.newTimeLeft)
            }
        }
    }

    val ControlledTimerState.isWork: Boolean
        get() = (this is ControlledTimerState.Running.Work)

    val ControlledTimerState.isRest: Boolean
        get() = (this is ControlledTimerState.Running.Rest)

    val ControlledTimerState.isLongRest: Boolean
        get() = (this is ControlledTimerState.Running.LongRest)

    private fun tryStartRest(state: ControlledTimerState.Running): ControlledTimerState? {
        if (!state.timerSettings.intervalsSettings.isEnabled) return null
        if (state !is ControlledTimerState.Running.Work) return null
        if (state.currentIteration >= state.maxIterations) return null

        return ControlledTimerState.Running.Rest(
            timeLeft = state.timerSettings.intervalsSettings.rest,
            isOnPause = false,
            timerSettings = state.timerSettings,
            currentIteration = state.currentIteration,
            maxIterations = state.maxIterations
        )
    }

    private fun tryStartLongRest(state: ControlledTimerState.Running): ControlledTimerState? {
        if (!state.timerSettings.intervalsSettings.isEnabled) return null
        if (state !is ControlledTimerState.Running.Work) return null
        if (state.currentIteration < state.maxIterations) return null

        return ControlledTimerState.Running.LongRest(
            timeLeft = state.timerSettings.intervalsSettings.longRest,
            isOnPause = false,
            timerSettings = state.timerSettings,
            currentIteration = state.currentIteration,
            maxIterations = state.maxIterations
        )
    }

    private fun tryStartWork(state: ControlledTimerState.Running): ControlledTimerState? {
        if (state.isWork) return null
        if (state.isLongRest) return null
        if (state.currentIteration >= state.maxIterations) return null

        return ControlledTimerState.Running.LongRest(
            timeLeft = state.timerSettings.intervalsSettings.work,
            isOnPause = false,
            timerSettings = state.timerSettings,
            currentIteration = state.currentIteration.plus(other = 1),
            maxIterations = state.maxIterations
        )
    }

    private fun tryStartFinish(state: ControlledTimerState.Running): ControlledTimerState? {
        if (!state.timerSettings.intervalsSettings.isEnabled) {
            if (!state.isWork) return null
            if (state.currentIteration < state.maxIterations) return null
        } else {
            if (!state.isLongRest) return null
            if (state.currentIteration < state.maxIterations) return null
        }
        return ControlledTimerState.Finished
    }

    private fun ControlledTimerState.onNoTimeLeft(msg: Message.NoTimeLeft): ControlledTimerState {
        return when (this) {
            ControlledTimerState.Finished, ControlledTimerState.NotStarted -> this
            is ControlledTimerState.Running -> {
                tryStartWork(this)
                    ?: tryStartRest(this)
                    ?: tryStartLongRest(this)
                    ?: tryStartFinish(this)
                    ?: this
            }
        }
    }

    fun ControlledTimerState.reduce(msg: Message): ControlledTimerState {
        return when (msg) {
            is Message.TimeUpdated -> onTimeUpdated(msg)
            is Message.NoTimeLeft -> onNoTimeLeft(msg)
        }
    }

    sealed interface Message {
        data class TimeUpdated(val newTimeLeft: Duration) : Message
        data object NoTimeLeft : Message
    }
}