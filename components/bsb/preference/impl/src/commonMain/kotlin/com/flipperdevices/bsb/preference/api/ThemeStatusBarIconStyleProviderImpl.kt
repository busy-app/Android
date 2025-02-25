package com.flipperdevices.bsb.preference.api

import com.flipperdevices.core.di.AppGraph
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(AppGraph::class, ThemeStatusBarIconStyleProvider::class)
class ThemeStatusBarIconStyleProviderImpl : ThemeStatusBarIconStyleProvider {
    override fun isStatusBarIconLight(systemIsDark: Boolean): Boolean {
        return true
    }
}
