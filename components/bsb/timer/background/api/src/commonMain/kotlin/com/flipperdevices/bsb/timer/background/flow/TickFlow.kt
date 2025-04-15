package com.flipperdevices.bsb.timer.background.flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration

/**
 * [TickFlow] is used to tick every [duration]
 */
class TickFlow(duration: Duration) : Flow<Unit> by flow(
    block = {
        while (true) {
            emit(Unit)
            delay(duration)
        }
    }
)
