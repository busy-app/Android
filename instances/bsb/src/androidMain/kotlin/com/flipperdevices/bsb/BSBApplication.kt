package com.flipperdevices.bsb

import android.app.Application
import com.flipperdevices.bsb.di.AndroidAppComponent
import com.flipperdevices.bsb.di.create
import com.flipperdevices.core.activityholder.CurrentActivityHolder
import com.flipperdevices.core.di.AndroidPlatformDependencies
import com.flipperdevices.core.di.ComponentHolder
import com.flipperdevices.core.ktx.common.FlipperDispatchers
import com.russhwolf.settings.SharedPreferencesSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber

class BSBApplication : Application() {
    private val androidAppComponent by lazy {
        AndroidAppComponent::class.create(
            observableSettingsDelegate = SharedPreferencesSettings(
                baseContext.getSharedPreferences(
                    "settings",
                    MODE_PRIVATE
                )
            ),
            scopeDelegate = CoroutineScope(SupervisorJob() + FlipperDispatchers.default),
            contextDelegate = this@BSBApplication,
            dependenciesDelegate = AndroidPlatformDependencies(MainActivity::class)
        )
    }

    override fun onCreate() {
        super.onCreate()

        CurrentActivityHolder.register(this)

        ComponentHolder.components += androidAppComponent

        Timber.plant(Timber.DebugTree())
        androidAppComponent.wearMessageSyncService.onCreate()
    }

    override fun onTerminate() {
        super.onTerminate()
        androidAppComponent.wearMessageSyncService.onDestroy()
    }
}
