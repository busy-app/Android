package com.flipperdevices.bsbwearable

import android.app.Application
import android.util.Log
import com.flipperdevices.bsb.wear.messenger.application.WearMessengerApplication
import com.flipperdevices.bsb.wear.messenger.di.WearMessengerModule
import com.flipperdevices.bsb.wear.messenger.model.PingMessage
import com.flipperdevices.bsbwearable.di.WearAppComponent
import com.flipperdevices.bsbwearable.di.create
import com.flipperdevices.core.activityholder.CurrentActivityHolder
import com.flipperdevices.core.di.AndroidPlatformDependencies
import com.flipperdevices.core.di.ComponentHolder
import com.flipperdevices.core.ktx.common.FlipperDispatchers
import com.russhwolf.settings.SharedPreferencesSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
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
    override val wearMessengerModule by lazy {
        WearMessengerModule.Default(
            applicationContext,
            applicationScope
        )
    }

    override fun onCreate() {
        super.onCreate()

        CurrentActivityHolder.register(this)

        ComponentHolder.components += WearAppComponent::class.create(
            settings,
            applicationScope,
            this@BSBApplication,
            AndroidPlatformDependencies(MainActivity::class)
        )

        Timber.plant(Timber.DebugTree())

        wearMessengerModule.wearMessageConsumer.messagesFlow
            .onEach { Log.d("BSBApplication", "#consumed: $it") }
            .launchIn(GlobalScope)
        GlobalScope.launch {
            while (currentCoroutineContext().isActive) {
                delay(1000L)
                val message = PingMessage
                Log.d("BSBApplication", "#sending: $message")
                wearMessengerModule.wearMessageProducer.produce(PingMessage, 0)
            }
        }
    }
}
