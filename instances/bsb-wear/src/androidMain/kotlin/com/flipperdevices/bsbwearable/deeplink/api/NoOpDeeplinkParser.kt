package com.flipperdevices.bsbwearable.deeplink.api

import android.content.Context
import android.content.Intent
import com.flipperdevices.bsb.deeplink.api.DeepLinkParserDelegate
import com.flipperdevices.bsb.deeplink.model.DeepLinkParserDelegatePriority
import com.flipperdevices.bsb.deeplink.model.Deeplink
import com.flipperdevices.core.di.AppGraph
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(AppGraph::class, DeepLinkParserDelegate::class, multibinding = true)
class NoOpDeeplinkParser : DeepLinkParserDelegate {
    override fun getPriority(
        context: Context,
        intent: Intent
    ): DeepLinkParserDelegatePriority? = null

    override suspend fun fromIntent(
        context: Context,
        intent: Intent
    ): Deeplink? = null
}
