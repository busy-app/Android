package com.flipperdevices.bsb.timer.background.newstatefactory.iteration.model

import kotlin.time.Duration

sealed interface IterationData {
    val iterationType: IterationType
    val startOffset: Duration

    data class Pending(
        override val startOffset: Duration,
        override val iterationType: IterationType.Await
    ) : IterationData

    data class Default(
        override val startOffset: Duration,
        val duration: Duration,
        override val iterationType: IterationType.Default
    ) : IterationData
}
