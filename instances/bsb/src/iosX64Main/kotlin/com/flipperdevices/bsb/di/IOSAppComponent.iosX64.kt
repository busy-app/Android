package com.flipperdevices.bsb.di

import com.russhwolf.settings.ObservableSettings
import kotlinx.coroutines.CoroutineScope

actual fun getIOSAppComponent(
    observableSettingsDelegate: ObservableSettings,
    scopeDelegate: CoroutineScope
): IOSAppComponent {
    return KotlinInjectIOSAppComponent::class.create(observableSettingsDelegate, scopeDelegate)
}
