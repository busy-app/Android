package com.flipperdevices.bsb.di

import com.flipperdevices.bsb.appblocker.api.FamilyControlApi
import com.flipperdevices.bsb.metronome.api.AudioPlayerApi
import com.flipperdevices.core.di.AppGraph
import com.russhwolf.settings.ObservableSettings
import kotlinx.coroutines.CoroutineScope
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.MergeComponent
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@MergeComponent(AppGraph::class)
@SingleIn(AppGraph::class)
abstract class IOSAppComponent(
    override val observableSettings: ObservableSettings,
    override val scope: CoroutineScope,
    @get:Provides val familyControlApi: FamilyControlApi,
    @get:Provides val audioPlayer: AudioPlayerApi
) : AppComponent

expect fun getIOSAppComponent(
    observableSettingsDelegate: ObservableSettings,
    scopeDelegate: CoroutineScope,
    familyControlApi: FamilyControlApi,
    audioPlayer: AudioPlayerApi
): IOSAppComponent
