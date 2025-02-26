package com.flipperdevices.bsb.timer.main.model

import com.flipperdevices.bsb.timer.delayedstart.api.DelayedStartScreenDecomposeComponent
import kotlinx.serialization.Serializable

@Serializable
sealed interface TimerMainNavigationConfig {
    @Serializable
    data object Main : TimerMainNavigationConfig

    @Serializable
    data object Work : TimerMainNavigationConfig

    @Serializable
    data object Rest : TimerMainNavigationConfig

    @Serializable
    data object LongRest : TimerMainNavigationConfig

    @Serializable
    data object Finished : TimerMainNavigationConfig

    @Serializable
    data class PauseAfter(
        val typeEndDelay: DelayedStartScreenDecomposeComponent.TypeEndDelay
    ) : TimerMainNavigationConfig
}
