package com.flipperdevices.core.apppackage

import com.flipperdevices.core.di.AppGraph
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(AppGraph::class, AppDetailedInfoProvider::class)
class AppDetailedInfoProviderNoop : AppDetailedInfoProvider {
    override suspend fun provideAppInfo(packageId: String) = null
}