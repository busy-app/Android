package com.flipperdevices.bsb.timer.background.statefactory.model

import kotlin.time.Duration

internal data class IterationRestrictions(
    val iterationTypeDuration: Duration,
    val isNoTimeForWorkLeft: Boolean,
    val isNoTimeForShortRestLeft: Boolean,
    val isLongRestNeedMoreTimeThanTimeLeft: Boolean,
    val resolvedIterationType: IterationType
)
