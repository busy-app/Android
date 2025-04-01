package com.flipperdevices.bsb.timer.background.statefactory

import com.flipperdevices.bsb.dao.model.TimerDuration
import com.flipperdevices.bsb.dao.model.TimerSettings
import com.flipperdevices.bsb.timer.background.statefactory.model.IterationData
import com.flipperdevices.bsb.timer.background.statefactory.model.IterationType
import com.flipperdevices.bsb.timer.background.statefactory.util.getRestrictions
import com.flipperdevices.bsb.timer.background.statefactory.util.getTypeByIndex
import com.flipperdevices.core.log.wtf
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.seconds

@Suppress("LongMethod", "CyclomaticComplexMethod")
fun TimerSettings.buildIterationList(): List<IterationData> {
    val totalTime = when (val localTotalTime = totalTime) {
        is TimerDuration.Finite -> localTotalTime.instance
        TimerDuration.Infinite -> {
            controlledTimerStateFactoryLogger.wtf {
                "#buildIterationList should not be called with infinite total time!"
            }
            1.hours
        }
    }
    if (!intervalsSettings.isEnabled) {
        return listOf(
            IterationData.Default(
                startOffset = 0.seconds,
                duration = totalTime,
                iterationType = IterationType.WORK
            )
        )
    }
    val list = buildList<IterationData> {
        var timeLeft = totalTime
        var i = 0
        while (timeLeft > 0.seconds) {
            val type = IterationType.getTypeByIndex(i)

            val restrictions = getRestrictions(
                timeLeft = timeLeft,
                type = type
            )
            if (restrictions.resolvedIterationType == IterationType.LONG_REST &&
                lastOrNull()?.iterationType == IterationType.LONG_REST
            ) {
                timeLeft -= restrictions.iterationTypeDuration
                i += 1
                continue
            }

            IterationData.Default(
                startOffset = totalTime - timeLeft,
                iterationType = restrictions.resolvedIterationType,
                duration = when {
                    restrictions.isNoTimeForShortRestLeft ->
                        restrictions.iterationTypeDuration
                            .plus(intervalsSettings.work)
                            .coerceAtMost(timeLeft)
                            .also { timeLeft -= intervalsSettings.work }

                    restrictions.isLongRestNeedMoreTimeThanTimeLeft ->
                        restrictions.iterationTypeDuration
                            .plus(intervalsSettings.longRest)
                            .coerceAtMost(timeLeft)
                            .also { timeLeft -= intervalsSettings.work }

                    restrictions.isNoTimeForWorkLeft -> restrictions.iterationTypeDuration
                    else -> restrictions.iterationTypeDuration
                }
            ).run(::add)
            if (listOf(
                    restrictions.isNoTimeForWorkLeft,
                    restrictions.isNoTimeForShortRestLeft,
                    restrictions.isLongRestNeedMoreTimeThanTimeLeft
                ).all { !it }
            ) {
                if (type == IterationType.WORK && !intervalsSettings.autoStartRest) {
                    IterationData.Pending(
                        startOffset = totalTime - timeLeft + restrictions.iterationTypeDuration,
                        iterationType = IterationType.WAIT_AFTER_WORK,
                        duration = Duration.INFINITE
                    ).run(::add)
                }
                if (type in listOf(
                        IterationType.REST,
                        IterationType.LONG_REST
                    ) && !intervalsSettings.autoStartWork
                ) {
                    IterationData.Pending(
                        startOffset = totalTime - timeLeft + restrictions.iterationTypeDuration,
                        iterationType = IterationType.WAIT_AFTER_REST,
                        duration = Duration.INFINITE
                    ).run(::add)
                }
            }

            timeLeft -= restrictions.iterationTypeDuration
            i += 1
        }
    }.toMutableList()
    if (list.isEmpty()) {
        controlledTimerStateFactoryLogger.wtf { "#buildIterationList was empty for $this" }
        return list
    }
    return list
}
