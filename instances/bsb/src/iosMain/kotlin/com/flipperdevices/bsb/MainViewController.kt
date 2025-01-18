package com.flipperdevices.bsb

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.bsb.appblocker.api.FamilyControlApi
import com.flipperdevices.bsb.di.getIOSAppComponent
import com.flipperdevices.bsb.metronome.api.AudioPlayerApi
import com.flipperdevices.bsb.preferencescreen.composable.LocalFamilyControlApi
import com.flipperdevices.core.ktx.common.FlipperDispatchers
import com.russhwolf.settings.Settings
import com.russhwolf.settings.observable.makeObservable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import platform.UIKit.UIViewController

@Suppress("FunctionNaming")
fun MainViewController(
    componentContext: ComponentContext,
    settings: Settings,
    familyControl: FamilyControlApi,
    audioPlayer: AudioPlayerApi
): UIViewController {
    val applicationScope = CoroutineScope(
        SupervisorJob() + FlipperDispatchers.default
    )
    val appComponent = getIOSAppComponent(
        settings.makeObservable(),
        applicationScope,
        familyControl,
        audioPlayer
    )
    val rootComponent = appComponent.rootDecomposeComponentFactory(
        componentContext,
        initialDeeplink = null
    )
    return ComposeUIViewController {
        CompositionLocalProvider(LocalFamilyControlApi provides familyControl) {
            App(rootComponent, appComponent)
        }
    }
}
