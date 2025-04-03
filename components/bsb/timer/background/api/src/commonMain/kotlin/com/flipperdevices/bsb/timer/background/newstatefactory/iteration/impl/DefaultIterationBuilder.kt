package com.flipperdevices.bsb.timer.background.newstatefactory.iteration.impl

import com.flipperdevices.bsb.dao.model.TimerSettings
import com.flipperdevices.bsb.timer.background.newstatefactory.iteration.IterationBuilder
import com.flipperdevices.bsb.timer.background.newstatefactory.iteration.model.IterationData
import com.flipperdevices.bsb.timer.background.newstatefactory.iteration.model.IterationType
import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.log.error
import com.flipperdevices.core.log.wtf
import kotlin.time.Duration
import kotlin.time.Duration.Companion.INFINITE
import kotlin.time.Duration.Companion.seconds

class DefaultIterationBuilder : IterationBuilder, LogTagProvider {
    override val TAG: String = "DefaultIterationBuilder"

    private fun getIterationTypeDuration(
        intervalsSettings: TimerSettings.IntervalsSettings,
        type: IterationType,
        timeLeft: Duration
    ): Duration {
        return when (type) {
            IterationType.WORK -> intervalsSettings.work
            IterationType.REST -> intervalsSettings.rest
            IterationType.LONG_REST -> intervalsSettings.longRest

            IterationType.WAIT_AFTER_REST,
            IterationType.WAIT_AFTER_WORK -> {
                wtf {
                    "#buildIterationList wait iteration appeared inside getIterationTypeByIndex"
                }
                0.seconds
            }
        }.coerceAtMost(timeLeft)
    }

    private fun getIterationTypeByIndex(i: Int): IterationType {
        return when {
            i % 2 == 0 -> IterationType.WORK
            i % 3 == 2 -> IterationType.LONG_REST
            i % 2 == 1 -> IterationType.REST
            else -> {
                error {
                    "#buildIterationList could not calculate next IterationType for index $i"
                }
                IterationType.WORK
            }
        }
    }

    override fun build(settings: TimerSettings, duration: Duration): List<IterationData> {
        var totalTimePassed = 0.seconds
        var iterationIndex = 0

        return buildList {
            while (totalTimePassed < duration) {
                val type = getIterationTypeByIndex(iterationIndex)
                val iterationDuration = getIterationTypeDuration(
                    intervalsSettings = settings.intervalsSettings,
                    type = type,
                    timeLeft = INFINITE
                )

                val lastIterationType = lastOrNull()?.iterationType

                val awaitIterationType = when {
                    !settings.intervalsSettings.autoStartRest && lastIterationType == IterationType.WORK -> {
                        IterationType.WAIT_AFTER_WORK
                    }

                    !settings.intervalsSettings.autoStartWork && lastIterationType == IterationType.REST -> {
                        IterationType.WAIT_AFTER_REST
                    }

                    !settings.intervalsSettings.autoStartWork && lastIterationType == IterationType.LONG_REST -> {
                        IterationType.WAIT_AFTER_REST
                    }

                    else -> null
                }
                if (awaitIterationType != null) {
                    IterationData.Pending(
                        startOffset = totalTimePassed,
                        duration = INFINITE,
                        iterationType = awaitIterationType
                    ).run(::add)
                }

                IterationData.Default(
                    startOffset = totalTimePassed,
                    duration = iterationDuration,
                    iterationType = type
                ).run(::add)
                totalTimePassed += iterationDuration
                iterationIndex++
            }
        }
    }
}
