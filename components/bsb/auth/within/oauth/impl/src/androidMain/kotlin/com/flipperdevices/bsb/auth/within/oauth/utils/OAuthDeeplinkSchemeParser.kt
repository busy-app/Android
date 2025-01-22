package com.flipperdevices.bsb.auth.within.oauth.utils

import android.content.Context
import android.content.Intent
import com.flipperdevices.bsb.cloud.model.BSBOAuthWebProvider
import com.flipperdevices.bsb.deeplink.api.DEEPLINK_BUSYAPP_SCHEME
import com.flipperdevices.bsb.deeplink.api.DeepLinkParserDelegate
import com.flipperdevices.bsb.deeplink.model.DeepLinkParserDelegatePriority
import com.flipperdevices.bsb.deeplink.model.Deeplink
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.ktx.android.toFullString
import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.log.info
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

private const val OAUTH_PATH_APPLE = "oauth_apple"
private const val OAUTH_PATH_MICROSOFT = "oauth_microsoft"

// Example of link: busyapp://oauth?authcode=9bb721ff-2b74-4066-8f77-529edebae6df
@Inject
@ContributesBinding(AppGraph::class, DeepLinkParserDelegate::class, multibinding = true)
class OAuthDeeplinkSchemeParser : DeepLinkParserDelegate, LogTagProvider {
    override val TAG = "OAuthDeeplinkSchemeParser"

    override fun getPriority(context: Context, intent: Intent): DeepLinkParserDelegatePriority? {
        if (intent.scheme != DEEPLINK_BUSYAPP_SCHEME) {
            return null
        }
        info { "Detect busy app scheme, try to parse ${intent.toFullString()}" }

        val intentData = intent.data ?: return null
        val host = intentData.host ?: return null

        if (host != OAUTH_PATH_APPLE && host != OAUTH_PATH_MICROSOFT) {
            return null
        }
        return DeepLinkParserDelegatePriority.DEFAULT
    }

    override suspend fun fromIntent(context: Context, intent: Intent): Deeplink? {
        info { "Try to parse ${intent.toFullString()}" }
        if (intent.scheme != DEEPLINK_BUSYAPP_SCHEME) {
            return null
        }

        val intentData = intent.data ?: return null
        val host = intentData.host ?: return null
        val authWay = when (host) {
            OAUTH_PATH_APPLE -> {
                BSBOAuthWebProvider.APPLE
            }
            OAUTH_PATH_MICROSOFT -> {
                BSBOAuthWebProvider.MICROSOFT
            }
            else -> return null
        }

        val authCode = intentData.getQueryParameter("auth_code") ?: return null

        return when (authWay) {
            BSBOAuthWebProvider.MICROSOFT -> Deeplink.Root.Auth.OAuth.Microsoft(authCode)
            BSBOAuthWebProvider.APPLE -> Deeplink.Root.Auth.OAuth.Apple(authCode)
        }
    }
}
