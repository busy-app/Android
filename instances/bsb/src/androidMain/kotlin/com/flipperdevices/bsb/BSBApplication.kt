package com.flipperdevices.bsb

import android.app.Application
import com.flipperdevices.bsb.di.AndroidAppComponent
import com.flipperdevices.bsb.di.create
import com.flipperdevices.bsb.wear.messenger.service.WearMessageSyncService
import com.flipperdevices.core.activityholder.CurrentActivityHolder
import com.flipperdevices.core.buildkonfig.BuildKonfig
import com.flipperdevices.core.di.AndroidPlatformDependencies
import com.flipperdevices.core.di.ComponentHolder
import com.flipperdevices.core.ktx.common.FlipperDispatchers
import com.russhwolf.settings.SharedPreferencesSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber

class BSBApplication : Application() {
    private val settings by lazy {
        SharedPreferencesSettings(
            baseContext.getSharedPreferences(
                "settings",
                MODE_PRIVATE
            )
        )
    }
    private val applicationScope = CoroutineScope(
        SupervisorJob() + FlipperDispatchers.default
    )
    private val wearMessageSyncService by lazy {
        WearMessageSyncService()
    }

    override fun onCreate() {
        super.onCreate()

        CurrentActivityHolder.register(this)

        val appComponent = AndroidAppComponent::class.create(
            settings,
            applicationScope,
            this@BSBApplication,
            AndroidPlatformDependencies(MainActivity::class)
        )
        ComponentHolder.components += appComponent

        if (BuildKonfig.IS_LOG_ENABLED) {
            Timber.plant(Timber.DebugTree())
        }
        if (BuildKonfig.IS_SENTRY_ENABLED) {
            appComponent.shake2ReportApi.init(this)
        }
        wearMessageSyncService.onCreate()
    }

    override fun onTerminate() {
        super.onTerminate()
        wearMessageSyncService.onDestroy()
    }
}
