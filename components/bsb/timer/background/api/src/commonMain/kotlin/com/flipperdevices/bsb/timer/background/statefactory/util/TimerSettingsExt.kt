package com.flipperdevices.bsb.timer.background.statefactory.util

import com.flipperdevices.bsb.dao.model.TimerSettings
import com.flipperdevices.bsb.timer.background.statefactory.buildIterationList
import com.flipperdevices.bsb.timer.background.statefactory.controlledTimerStateFactoryLogger
import com.flipperdevices.bsb.timer.background.statefactory.model.IterationRestrictions
import com.flipperdevices.bsb.timer.background.statefactory.model.IterationType
import com.flipperdevices.core.log.wtf
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

internal fun TimerSettings.getIterationTypeDuration(
    type: IterationType,
    timeLeft: Duration
): Duration {
    return when (type) {
        IterationType.WORK -> intervalsSettings.work
        IterationType.REST -> intervalsSettings.rest
        IterationType.LONG_REST -> intervalsSettings.longRest

        IterationType.WAIT_AFTER_REST,
        IterationType.WAIT_AFTER_WORK -> {
            controlledTimerStateFactoryLogger.wtf {
                "#buildIterationList wait iteration appeared inside getIterationTypeByIndex"
            }
            0.seconds
        }
    }.coerceAtMost(timeLeft)
}

internal val TimerSettings.maxIterationCount: Int
    get() = buildIterationList()
        .count { data -> data.iterationType == IterationType.WORK }

internal fun TimerSettings.getRestrictions(
    timeLeft: Duration,
    type: IterationType
): IterationRestrictions {
    val iterationTypeDuration = getIterationTypeDuration(
        type = type,
        timeLeft = timeLeft
    )

    val isNoTimeForWorkLeft = timeLeft <= iterationTypeDuration &&
        type == IterationType.WORK

    val isNoTimeForShortRestLeft =
        timeLeft <= (iterationTypeDuration + intervalsSettings.work) &&
            type == IterationType.REST

    val isLongRestNeedMoreTimeThanTimeLeft =
        timeLeft <= (iterationTypeDuration + intervalsSettings.longRest) &&
            type == IterationType.LONG_REST

    val resolvedIterationType = when {
        isNoTimeForWorkLeft
            .or(isNoTimeForShortRestLeft)
            .or(isLongRestNeedMoreTimeThanTimeLeft) -> IterationType.LONG_REST

        else -> type
    }

    return IterationRestrictions(
        iterationTypeDuration = iterationTypeDuration,
        isNoTimeForWorkLeft = isNoTimeForWorkLeft,
        isNoTimeForShortRestLeft = isNoTimeForShortRestLeft,
        isLongRestNeedMoreTimeThanTimeLeft = isLongRestNeedMoreTimeThanTimeLeft,
        resolvedIterationType = resolvedIterationType
    )
}
