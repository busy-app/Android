package com.flipperdevices.bsb.di

import com.flipperdevices.bsb.appblocker.api.FamilyControlApi
import com.flipperdevices.bsb.metronome.api.AudioPlayerApi
import com.russhwolf.settings.ObservableSettings
import kotlinx.coroutines.CoroutineScope

actual fun getIOSAppComponent(
    observableSettingsDelegate: ObservableSettings,
    scopeDelegate: CoroutineScope,
    familyControlApi: FamilyControlApi,
    audioPlayer: AudioPlayerApi
): IOSAppComponent {
    return KotlinInjectIOSAppComponent::class.create(
        observableSettingsDelegate,
        scopeDelegate,
        familyControlApi,
        audioPlayer
    )
}
