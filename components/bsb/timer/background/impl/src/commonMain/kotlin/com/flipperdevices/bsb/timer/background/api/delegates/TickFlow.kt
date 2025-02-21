package com.flipperdevices.bsb.timer.background.api.delegates

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * [TickFlow] is used to tick every [duration]
 */
internal class TickFlow(duration: Duration = 1.seconds) : Flow<Unit> by flow(
    block = {
        while (true) {
            emit(Unit)
            delay(duration)
        }
    }
)
