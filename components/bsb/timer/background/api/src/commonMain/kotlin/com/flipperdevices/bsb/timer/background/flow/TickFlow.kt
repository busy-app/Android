package com.flipperdevices.bsb.timer.background.flow

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * [TickFlow] is used to tick every [duration]
 */
class TickFlow(duration: Duration = 1.seconds) : Flow<Unit> by flow(
    block = {
        while (currentCoroutineContext().isActive) {
            emit(Unit)
            delay(duration)
        }
    }
)
