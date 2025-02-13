package com.flipperdevices.bsb.appblocker.deeplink

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.flipperdevices.bsb.appblocker.model.ApplicationInfo
import com.flipperdevices.bsb.deeplink.api.DeepLinkParserDelegate
import com.flipperdevices.bsb.deeplink.model.DeepLinkParserDelegatePriority
import com.flipperdevices.bsb.deeplink.model.Deeplink
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.ktx.android.toFullString
import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.log.error
import com.flipperdevices.core.log.info
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import kotlin.reflect.KClass

private const val ACTION = "com.flipperdevices.bsb.appblocker.deeplink.AppBlockDeeplinkParser"
private const val EXTRA_PACKAGE_NAME = "package_name"

@Inject
@ContributesBinding(AppGraph::class, DeepLinkParserDelegate::class, multibinding = true)
class AppBlockDeeplinkParser : DeepLinkParserDelegate, LogTagProvider {
    override val TAG = "AppBlockDeeplinkParser"

    override fun getPriority(context: Context, intent: Intent): DeepLinkParserDelegatePriority? {
        if (intent.action == ACTION) {
            return DeepLinkParserDelegatePriority.HIGH
        }
        return null
    }

    override suspend fun fromIntent(context: Context, intent: Intent): Deeplink? {
        info { "Start parsing ${intent.toFullString()}" }
        if (intent.action != ACTION) {
            return null
        }
        val packageName = intent.getStringExtra(EXTRA_PACKAGE_NAME)
        if (packageName == null) {
            error { "Failed parse intent, because package name not found" }
            return null
        }

        val applicationInfo = context.packageManager.getApplicationInfo(packageName, 0)

        return Deeplink.Root.AppLockScreen(
            ApplicationInfo(
                name = applicationInfo.loadLabel(context.packageManager).toString()
            )
        )
    }

    companion object {
        fun getIntent(
            context: Context,
            packageName: String,
            activity: KClass<out Activity>
        ): Intent {
            val intent = Intent(context, activity.java)
            intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP or
                    Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
            )
            intent.setAction(ACTION)
            intent.putExtra(EXTRA_PACKAGE_NAME, packageName)
            return intent
        }
    }
}
