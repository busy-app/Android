package com.flipperdevices.bsb.analytics.metric.api

import com.flipperdevices.bsb.analytics.metric.api.model.BEvent
import com.flipperdevices.core.di.AppGraph
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppGraph::class)
@ContributesBinding(AppGraph::class, MetricApi::class)
class NoOpMetricApi : MetricApi {
    override fun reportEvent(event: BEvent) = Unit
}
