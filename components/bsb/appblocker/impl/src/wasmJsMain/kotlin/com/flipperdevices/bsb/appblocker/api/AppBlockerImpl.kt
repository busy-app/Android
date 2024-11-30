package com.flipperdevices.bsb.appblocker.api

import com.flipperdevices.core.di.AppGraph
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(AppGraph::class, AppBlockerApi::class)
class AppBlockerImpl : AppBlockerApi {
    override fun isAppBlockerSupportActive() = false
    override fun enableSupport() = Result.failure<Unit>(NotImplementedError())
    override fun disableSupport() = Unit
}