package com.flipperdevices.bsb.appblocker.api

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.flipperdevices.bsb.appblocker.model.ApplicationInfo
import kotlin.reflect.KClass

interface ApplicationInfoIntentParserApi {
    fun isApplicationInfoAction(action: String?): Boolean

    fun getIntent(
        applicationInfo: ApplicationInfo,
        context: Context,
        activity: KClass<out Activity>
    ): Intent

    fun parse(intent: Intent): Result<ApplicationInfo>
}
