package com.flipperdevices.bsb.timer.background.newstatefactory.iteration.impl

import com.flipperdevices.bsb.dao.model.TimerDuration
import com.flipperdevices.bsb.dao.model.TimerSettings
import com.flipperdevices.bsb.timer.background.newstatefactory.iteration.IterationBuilder
import com.flipperdevices.bsb.timer.background.newstatefactory.iteration.model.IterationData
import com.flipperdevices.bsb.timer.background.newstatefactory.iteration.model.IterationType
import kotlin.time.Duration

class RestrictedIterationBuilder(private val instance: IterationBuilder) : IterationBuilder {
    override fun build(settings: TimerSettings, duration: Duration): List<IterationData> {
        val iterations = instance.build(settings, duration)
        val lastIteration = iterations.lastOrNull() ?: return iterations
        val totalDuration = lastIteration.duration.plus(lastIteration.startOffset)
        val totalTime = (settings.totalTime as? TimerDuration.Finite)
            ?.instance
            ?: return iterations
        if (totalDuration <= totalTime) return iterations
        return when (lastIteration.iterationType) {
            IterationType.REST -> {
                (iterations - lastIteration) + IterationData.Default(
                    startOffset = lastIteration.startOffset,
                    duration = lastIteration.duration,
                    iterationType = IterationType.LONG_REST
                )
            }

            IterationType.WORK -> {
                buildList {
                    addAll(iterations)
                    if (!settings.intervalsSettings.autoStartRest) {
                        IterationData.Default(
                            startOffset = lastIteration.startOffset + lastIteration.duration,
                            duration = Duration.INFINITE,
                            iterationType = IterationType.WAIT_AFTER_WORK
                        ).run(::add)
                    }
                    IterationData.Default(
                        startOffset = lastIteration.startOffset + lastIteration.duration,
                        duration = settings.intervalsSettings.longRest,
                        iterationType = IterationType.LONG_REST
                    ).run(::add)
                }
            }

            IterationType.LONG_REST,
            IterationType.WAIT_AFTER_REST,
            IterationType.WAIT_AFTER_WORK -> iterations
        }
    }
}
