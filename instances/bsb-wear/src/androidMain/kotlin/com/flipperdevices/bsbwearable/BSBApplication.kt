package com.flipperdevices.bsbwearable

import android.app.Application
import com.flipperdevices.bsb.wear.messenger.application.WearMessengerApplication
import com.flipperdevices.bsb.wear.messenger.di.WearMessengerModule
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

internal class BSBApplication : Application(), WearMessengerApplication {
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
    override lateinit var wearMessengerModule: WearMessengerModule
    override fun onCreate() {
        super.onCreate()

        CurrentActivityHolder.register(this)

        ComponentHolder.components += WearAppComponent::class.create(
            settings,
            applicationScope,
            this@BSBApplication,
            AndroidPlatformDependencies(MainActivity::class)
        )

        wearMessengerModule = ComponentHolder.component<WearAppComponent>().wearMessengerModule

        Timber.plant(Timber.DebugTree())
    }
}
