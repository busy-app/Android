package com.flipperdevices.bsb.di

import com.flipperdevices.core.di.AppGraph
import com.russhwolf.settings.ObservableSettings
import kotlinx.coroutines.CoroutineScope
import software.amazon.lastmile.kotlin.inject.anvil.MergeComponent
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@MergeComponent(AppGraph::class)
@SingleIn(AppGraph::class)
abstract class IOSAppComponent(
    override val observableSettings: ObservableSettings,
    override val scope: CoroutineScope
) : AppComponent

expect fun getIOSAppComponent(observableSettingsDelegate: ObservableSettings, scopeDelegate: CoroutineScope): IOSAppComponent
