package com.flipperdevices.bsb.timer.main.model

import com.flipperdevices.bsb.dao.model.TimerSettings
import com.flipperdevices.bsb.timer.delayedstart.api.DelayedStartScreenDecomposeComponent
import kotlinx.serialization.Serializable

@Serializable
sealed interface TimerMainNavigationConfig {

    @Serializable
    data object Main : TimerMainNavigationConfig

    @Serializable
    data class Work(
        val timerSettings: TimerSettings
    )  : TimerMainNavigationConfig

    @Serializable
    data class Rest(
        val timerSettings: TimerSettings
    )  : TimerMainNavigationConfig

    @Serializable
    data class LongRest(
        val timerSettings: TimerSettings
    )  : TimerMainNavigationConfig

    @Serializable
    data class Finished(
        val timerSettings: TimerSettings
    )  : TimerMainNavigationConfig

    @Serializable
    data class PauseAfter(
        val typeEndDelay: DelayedStartScreenDecomposeComponent.TypeEndDelay,
        val timerSettings: TimerSettings
    ) : TimerMainNavigationConfig
}
