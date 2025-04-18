package com.flipperdevices.bsb.timer.cards.model

import busystatusbar.components.bsb.timer.cards.impl.generated.resources.Res
import busystatusbar.components.bsb.timer.cards.impl.generated.resources.img_dots_first_frame
import busystatusbar.components.bsb.timer.cards.impl.generated.resources.img_particles_first_frame
import busystatusbar.components.bsb.timer.cards.impl.generated.resources.img_rock_first_frame
import com.flipperdevices.bsb.dao.model.TimerSettings
import com.flipperdevices.bsb.dao.model.TimerSettingsId
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi

data class PagerData(
    val videoUri: String,
    val firstFrame: DrawableResource,
    val timerSettings: TimerSettings
)

@OptIn(ExperimentalResourceApi::class)
val EXAMPLE_DATA: ImmutableList<PagerData>
    get() = persistentListOf(
        PagerData(
            videoUri = Res.getUri("files/dots.mp4"),
            firstFrame = Res.drawable.img_dots_first_frame,
            timerSettings = TimerSettings(
                name = "BUSY",
                id = TimerSettingsId(-1)
            )
        ),
        PagerData(
            videoUri = Res.getUri("files/rock.mp4"),
            firstFrame = Res.drawable.img_rock_first_frame,
            timerSettings = TimerSettings(
                name = "SQUEZY",
                id = TimerSettingsId(-1),
                intervalsSettings = TimerSettings.IntervalsSettings(
                    isEnabled = true
                )
            )
        ),
        PagerData(
            videoUri = Res.getUri("files/particles.mp4"),
            firstFrame = Res.drawable.img_particles_first_frame,
            timerSettings = TimerSettings(
                name = "WIZZY",
                id = TimerSettingsId(-1)
            )
        )
    )
