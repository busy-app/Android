package com.flipperdevices.bsbwearable

import android.app.Application
import com.flipperdevices.bsbwearable.di.WearAppComponent
import com.flipperdevices.bsbwearable.di.create
import com.flipperdevices.core.activityholder.CurrentActivityHolder
import com.flipperdevices.core.di.AndroidPlatformDependencies
import com.flipperdevices.core.di.ComponentHolder
import com.flipperdevices.core.ktx.common.FlipperDispatchers
import com.russhwolf.settings.SharedPreferencesSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber

internal class BSBApplication : Application() {
    private val wearAppComponent by lazy {
        WearAppComponent::class.create(
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

        ComponentHolder.components += wearAppComponent

        Timber.plant(Timber.DebugTree())
        wearAppComponent.wearMessageSyncService.onCreate()
    }

    override fun onTerminate() {
        super.onTerminate()
        wearAppComponent.wearMessageSyncService.onDestroy()
    }
}
