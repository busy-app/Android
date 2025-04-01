package com.flipperdevices.bsb.timer.background.statefactory.util

import com.flipperdevices.bsb.timer.background.statefactory.controlledTimerStateFactoryLogger
import com.flipperdevices.bsb.timer.background.statefactory.model.IterationType
import com.flipperdevices.core.log.error

@Suppress("MagicNumber")
internal fun IterationType.Companion.getTypeByIndex(i: Int): IterationType {
    return when {
        i % 2 == 0 -> IterationType.WORK
        i % 3 == 2 -> IterationType.LONG_REST
        i % 2 == 1 -> IterationType.REST
        else -> {
            controlledTimerStateFactoryLogger.error {
                "#buildIterationList could not calculate next IterationType for index $i"
            }
            IterationType.WORK
        }
    }
}
