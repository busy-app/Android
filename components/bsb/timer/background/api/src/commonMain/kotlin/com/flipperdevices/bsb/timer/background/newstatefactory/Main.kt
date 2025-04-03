package com.flipperdevices.bsb.timer.background.newstatefactory

import com.flipperdevices.bsb.dao.model.TimerDuration
import com.flipperdevices.bsb.dao.model.TimerSettings
import com.flipperdevices.bsb.dao.model.TimerSettingsId
import com.flipperdevices.bsb.timer.background.model.TimerTimestamp
import com.flipperdevices.bsb.timer.background.newstatefactory.iteration.impl.DefaultIterationBuilder
import com.flipperdevices.bsb.timer.background.newstatefactory.iteration.impl.RestrictedIterationBuilder
import com.flipperdevices.bsb.timer.background.newstatefactory.state.TimerStateFactory
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.minutes

private fun printIterations(settings: TimerSettings) {
    DefaultIterationBuilder().build(settings, 90.minutes).onEach {
        println(it)
    }
    println("\nDecorated:")
    RestrictedIterationBuilder(DefaultIterationBuilder()).build(settings, 90.minutes).onEach {
        println(it)
    }
}

fun main() {
    TimerSettings(
        id = TimerSettingsId(-1),
        totalTime = TimerDuration(90.minutes),
        intervalsSettings = TimerSettings.IntervalsSettings(
            work = 30.minutes,
            rest = 10.minutes,
            longRest = 30.minutes
        )
    ).run(::printIterations)
    println("\nInfinite:")
    TimerSettings(
        id = TimerSettingsId(-1),
        totalTime = TimerDuration.Infinite,
        intervalsSettings = TimerSettings.IntervalsSettings(
            work = 30.minutes,
            rest = 10.minutes,
            longRest = 30.minutes
        )
    ).run(::printIterations)

    println("\nTimerStateFactory:")
    println(
        TimerStateFactory.create(
            TimerTimestamp.Running(
                lastSync = Clock.System.now(),
                settings = TimerSettings(
                    id = TimerSettingsId(-1),
                    totalTime = TimerDuration.Infinite,
                    intervalsSettings = TimerSettings.IntervalsSettings(
                        work = 30.minutes,
                        rest = 10.minutes,
                        longRest = 30.minutes
                    )
                )
            )
        )
    )
}
