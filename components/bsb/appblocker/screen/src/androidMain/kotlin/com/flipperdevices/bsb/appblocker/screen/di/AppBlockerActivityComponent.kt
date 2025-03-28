package com.flipperdevices.bsb.appblocker.screen.di

import com.flipperdevices.bsb.analytics.metric.api.MetricApi
import com.flipperdevices.bsb.appblocker.api.ApplicationInfoIntentParserApi

interface AppBlockerActivityComponent {
    val applicationInfoParserApi: ApplicationInfoIntentParserApi
    val metricApi: MetricApi
}
