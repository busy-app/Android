package com.flipperdevices.busybar.di

import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.busybar.root.api.RootDecomposeComponent
import com.russhwolf.settings.Settings
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.KmpComponentCreate
import me.tatarka.inject.annotations.Provides

@Component
abstract class AppComponent(
    @get:Provides protected val settings: Settings,
    @Component val httpClientComponent: HttpClientComponent
) {
    abstract val rootComponent: (componentContext: ComponentContext) -> RootDecomposeComponent
}

@KmpComponentCreate
expect fun createAppComponent(
    settings: Settings,
    httpClientComponent: HttpClientComponent
): AppComponent