package com.flipperdevices.bsb.analytics.metric.api.model

import kotlinx.serialization.SerialName

data class TimerConfigSnapshot(
    @SerialName("intervals")
    val isIntervalsEnabled: Boolean,
    @SerialName("total_time")
    val totalTimeMillis: Long,
    @SerialName("work_time")
    val workTimerMillis: Long,
    @SerialName("rest_time")
    val restTimeMillis: Long,
    @SerialName("blockings_enabled")
    val isBlockingEnabled: Boolean,
    @SerialName("blocking_categories")
    val blockingCategories: List<String>
)
