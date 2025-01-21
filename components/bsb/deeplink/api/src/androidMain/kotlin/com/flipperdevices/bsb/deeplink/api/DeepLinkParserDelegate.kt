package com.flipperdevices.bsb.deeplink.api

import android.content.Context
import android.content.Intent
import com.flipperdevices.bsb.deeplink.model.DeepLinkParserDelegatePriority
import com.flipperdevices.bsb.deeplink.model.Deeplink

const val DEEPLINK_BUSYAPP_SCHEME = "busyapp"

interface DeepLinkParserDelegate {
    fun getPriority(context: Context, intent: Intent): DeepLinkParserDelegatePriority?
    suspend fun fromIntent(context: Context, intent: Intent): Deeplink?
}
