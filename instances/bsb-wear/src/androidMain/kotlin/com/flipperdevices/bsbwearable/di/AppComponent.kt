package com.flipperdevices.bsbwearable.di

import com.flipperdevices.bsb.wear.messenger.service.WearMessageSyncService
import com.flipperdevices.bsbwearable.root.api.RootDecomposeComponent
import com.russhwolf.settings.ObservableSettings
import kotlinx.coroutines.CoroutineScope
import me.tatarka.inject.annotations.Provides

interface AppComponent {
    @get:Provides
    val observableSettings: ObservableSettings

    @get:Provides
    val scope: CoroutineScope

    val rootDecomposeComponentFactory: RootDecomposeComponent.Factory
    val wearMessageSyncService: WearMessageSyncService
}
