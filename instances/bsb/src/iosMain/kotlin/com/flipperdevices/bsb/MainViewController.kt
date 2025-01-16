package com.flipperdevices.bsb

import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.bsb.appblocker.api.FamilyControlApi
import com.flipperdevices.bsb.di.getIOSAppComponent
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
    familyControl: FamilyControlApi
): UIViewController {
    val applicationScope = CoroutineScope(
        SupervisorJob() + FlipperDispatchers.default
    )
    val appComponent = getIOSAppComponent(
        settings.makeObservable(),
        applicationScope,
        familyControl
    )
    val rootComponent = appComponent.rootDecomposeComponentFactory(
        componentContext,
        initialDeeplink = null
    )
    return ComposeUIViewController {
        App(rootComponent, appComponent)
    }
}
