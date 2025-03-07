package com.flipperdevices.bsbwearable.di

import android.content.Context
import com.flipperdevices.core.di.AndroidPlatformDependencies
import com.flipperdevices.core.di.AppGraph
import com.russhwolf.settings.ObservableSettings
import kotlinx.coroutines.CoroutineScope
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.MergeComponent
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@MergeComponent(AppGraph::class)
@SingleIn(AppGraph::class)
internal abstract class WearAppComponent(
    override val observableSettings: ObservableSettings,
    override val scope: CoroutineScope,
    @get:Provides val context: Context,
    @get:Provides val dependencies: AndroidPlatformDependencies,
) : AppComponent
