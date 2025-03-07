package com.flipperdevices.bsb.appblocker.api

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.flipperdevices.bsb.appblocker.model.ApplicationInfo
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.ktx.android.toFullString
import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.log.info
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import kotlin.reflect.KClass

private const val ACTION = "com.flipperdevices.bsb.appblocker.deeplink.AppBlockDeeplinkParser"
private const val EXTRA_PACKAGE_NAME = "package_name"
private const val EXTRA_OPEN_COUNT = "open_count"

@Inject
@ContributesBinding(AppGraph::class, ApplicationInfoIntentParserApi::class)
class ApplicationInfoIntentParserApiImpl : ApplicationInfoIntentParserApi, LogTagProvider {
    override val TAG = "ApplicationInfoIntentParserApiImpl"

    override fun isApplicationInfoAction(action: String?): Boolean {
        return action == ACTION
    }

    override fun getIntent(
        applicationInfo: ApplicationInfo,
        context: Context,
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
        intent.putExtra(EXTRA_PACKAGE_NAME, applicationInfo.packageName)
        intent.putExtra(EXTRA_OPEN_COUNT, applicationInfo.openCount)
        return intent
    }

    override fun parse(intent: Intent): Result<ApplicationInfo> {
        info { "Start parsing ${intent.toFullString()}" }
        if (intent.action != ACTION) {
            return Result.failure(IllegalArgumentException("Can't find action $ACTION in intent"))
        }
        val packageName = intent.getStringExtra(EXTRA_PACKAGE_NAME)
            ?: return Result.failure(
                IllegalArgumentException("Failed parse intent, because package name not found")
            )
        val openCount = intent.getIntExtra(EXTRA_OPEN_COUNT, 0)

        return Result.success(
            ApplicationInfo(
                packageName = packageName,
                openCount = openCount
            )
        )
    }
}
