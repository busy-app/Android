package com.flipperdevices.bsb.timer.cards.model

import busystatusbar.components.bsb.timer.cards.impl.generated.resources.Res
import com.flipperdevices.bsb.dao.model.TimerSettings
import com.flipperdevices.bsb.dao.model.TimerSettingsId
import org.jetbrains.compose.resources.ExperimentalResourceApi

data class PagerData(
    val videoUri: String,
    val timerSettings: TimerSettings
)

@OptIn(ExperimentalResourceApi::class)
val EXAMPLE_DATA: List<PagerData>
    get() = listOf(
        PagerData(
            Res.getUri("files/dots.mp4"),
            TimerSettings(
                name = "BUSY",
                id = TimerSettingsId(-1)
            )
        ),
        PagerData(
            Res.getUri("files/rock.mp4"),
            TimerSettings(
                name = "SQUEZY",
                id = TimerSettingsId(-1),
                intervalsSettings = TimerSettings.IntervalsSettings(
                    isEnabled = true
                )
            )
        ),
        PagerData(
            Res.getUri("files/particles.mp4"),
            TimerSettings(
                name = "WIZZY",
                id = TimerSettingsId(-1)
            )
        )
    )