package com.flipperdevices.bsb.timer.background.newstatefactory.iteration.impl

import com.flipperdevices.bsb.dao.model.TimerDuration
import com.flipperdevices.bsb.dao.model.TimerSettings
import com.flipperdevices.bsb.timer.background.newstatefactory.iteration.IterationBuilder
import com.flipperdevices.bsb.timer.background.newstatefactory.iteration.model.IterationData
import com.flipperdevices.bsb.timer.background.newstatefactory.iteration.model.IterationType
import com.flipperdevices.core.log.LogTagProvider
import kotlin.time.Duration
import kotlin.time.Duration.Companion.INFINITE
import kotlin.time.Duration.Companion.seconds

class DefaultIterationBuilder : IterationBuilder, LogTagProvider {
    override val TAG: String = "DefaultIterationBuilder"

    private fun getIterationTypeDuration(
        intervalsSettings: TimerSettings.IntervalsSettings,
        type: IterationType.Default,
        timeLeft: Duration
    ): Duration {
        return when (type) {
            IterationType.Default.WORK -> intervalsSettings.work
            IterationType.Default.REST -> intervalsSettings.rest
            IterationType.Default.LONG_REST -> intervalsSettings.longRest
        }.coerceAtMost(timeLeft)
    }

    @Suppress("MagicNumber")
    private fun getIterationTypeByIndex(i: Int): IterationType.Default {
        return when {
            i % 2 == 0 -> IterationType.Default.WORK
            i % 3 == 2 -> IterationType.Default.LONG_REST
            i % 2 == 1 -> IterationType.Default.REST
            else -> IterationType.Default.REST
        }
    }

    private fun getPendingIteration(
        settings: TimerSettings,
        lastIterationType: IterationType?,
        totalTimePassed: Duration
    ): IterationData.Pending? {
        return when {
            !settings.intervalsSettings.autoStartRest && lastIterationType == IterationType.Default.WORK -> {
                IterationData.Pending(
                    startOffset = totalTimePassed,
                    iterationType = IterationType.Await.WAIT_AFTER_WORK
                )
            }

            !settings.intervalsSettings.autoStartWork && lastIterationType == IterationType.Default.REST -> {
                IterationData.Pending(
                    startOffset = totalTimePassed,
                    iterationType = IterationType.Await.WAIT_AFTER_REST
                )
            }

            !settings.intervalsSettings.autoStartWork && lastIterationType == IterationType.Default.LONG_REST -> {
                IterationData.Pending(
                    startOffset = totalTimePassed,
                    iterationType = IterationType.Await.WAIT_AFTER_REST
                )
            }

            else -> null
        }
    }

    override fun build(settings: TimerSettings, duration: Duration): List<IterationData> {
        if (!settings.intervalsSettings.isEnabled) {
            val iterationData = when (val localTotalTime = settings.totalTime) {
                is TimerDuration.Finite -> IterationData.Default(
                    startOffset = 0.seconds,
                    duration = localTotalTime.instance,
                    iterationType = IterationType.Default.WORK
                )

                TimerDuration.Infinite -> IterationData.Infinite(
                    startOffset = 0.seconds,
                    iterationType = IterationType.Default.WORK
                )
            }
            return listOf(iterationData)
        }

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

                val lastIterationType = filterIsInstance<IterationData.Default>()
                    .lastOrNull()
                    ?.iterationType

                getPendingIteration(
                    settings = settings,
                    lastIterationType = lastIterationType,
                    totalTimePassed = totalTimePassed
                )?.run(::add)

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
