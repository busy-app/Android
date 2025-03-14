package com.flipperdevices.bsb.analytics.shake2report.api

import android.app.Application
import com.flipperdevices.core.di.AppGraph
import io.sentry.SentryLevel
import io.sentry.android.core.SentryAndroid
import io.sentry.android.timber.SentryTimberIntegration
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(AppGraph::class, Shake2ReportApi::class)
class Shake2ReportApiImpl : Shake2ReportApi {
    override fun init(application: Application) {
        SentryAndroid.init(application) { options ->
            options.addIntegration(
                SentryTimberIntegration(
                    minEventLevel = SentryLevel.FATAL,
                    minBreadcrumbLevel = SentryLevel.DEBUG
                )
            )
        }
    }
}
