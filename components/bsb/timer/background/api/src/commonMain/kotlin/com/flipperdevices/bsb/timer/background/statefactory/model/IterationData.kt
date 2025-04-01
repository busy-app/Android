package com.flipperdevices.bsb.timer.background.statefactory.model

import kotlin.time.Duration

sealed interface IterationData {
    val iterationType: IterationType
    val startOffset: Duration
    val duration: Duration

    data class Pending(
        override val startOffset: Duration,
        override val iterationType: IterationType,
        override val duration: Duration
    ) : IterationData

    data class Default(
        override val startOffset: Duration,
        override val duration: Duration,
        override val iterationType: IterationType
    ) : IterationData
}
