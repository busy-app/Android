package com.flipperdevices.bsb.analytics.metric.api

import com.flipperdevices.bsb.analytics.metric.api.model.BEvent

interface MetricApi {
    fun reportEvent(event: BEvent)
}
