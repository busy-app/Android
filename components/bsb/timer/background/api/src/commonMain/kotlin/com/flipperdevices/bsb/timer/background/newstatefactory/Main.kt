package com.flipperdevices.bsb.timer.background.newstatefactory

import com.flipperdevices.bsb.dao.model.TimerDuration
import com.flipperdevices.bsb.dao.model.TimerSettings
import com.flipperdevices.bsb.dao.model.TimerSettingsId
import com.flipperdevices.bsb.timer.background.newstatefactory.iteration.impl.CoercedIterationBuilder
import com.flipperdevices.bsb.timer.background.newstatefactory.iteration.impl.DefaultIterationBuilder
import kotlin.time.Duration.Companion.minutes

private fun printIterations(settings: TimerSettings) {
    DefaultIterationBuilder().build(settings, 20.minutes).onEach {
        println(it)
    }
    println("\nDecorated:")
    CoercedIterationBuilder(DefaultIterationBuilder()).build(settings, 20.minutes).onEach {
        println(it)
    }
}

fun main() {
    TimerSettings(
        id = TimerSettingsId(-1),
        totalTime = TimerDuration.Finite(25.minutes),
        intervalsSettings = TimerSettings.IntervalsSettings(
            work = 25.minutes,
            rest = 15.minutes,
            longRest = 30.minutes,
            isEnabled = true
        )
    ).run(::printIterations)

//    println("\nInfinite:")
//    TimerSettings(
//        id = TimerSettingsId(-1),
//        totalTime = TimerDuration.Infinite,
//        intervalsSettings = TimerSettings.IntervalsSettings(
//            work = 25.minutes,
//            rest = 15.minutes,
//            longRest = 30.minutes,
//            isEnabled = false
//        )
//    ).run(::printIterations)
//
//    println("\nTimerStateFactory:")
//    println(
//        TimerStateFactory.create(
//            TimerTimestamp.Running(
//                lastSync = Clock.System.now(),
//                settings = TimerSettings(
//                    id = TimerSettingsId(-1),
//                    totalTime = TimerDuration.Infinite,
//                    intervalsSettings = TimerSettings.IntervalsSettings(
//                        work = 30.minutes,
//                        rest = 10.minutes,
//                        longRest = 30.minutes
//                    )
//                )
//            )
//        )
//    )
}
