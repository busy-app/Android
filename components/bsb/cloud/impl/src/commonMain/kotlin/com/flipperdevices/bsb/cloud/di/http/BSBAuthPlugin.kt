package com.flipperdevices.bsb.cloud.di.http

import com.flipperdevices.bsb.preference.api.PreferenceApi
import com.flipperdevices.bsb.preference.model.SettingsEnum
import io.ktor.client.plugins.api.Send
import io.ktor.client.plugins.api.createClientPlugin

val BSBAuthPlugin = createClientPlugin("BSBAuthPlugin", ::AuthPluginConfig) {
    on(Send) { request ->
        val token = pluginConfig.preferenceApi?.getString(SettingsEnum.AUTH_TOKEN, null)
        if (token != null) {
            request.headers.append("Authorization", token)
        }
        proceed(request)
    }
}

class AuthPluginConfig {
    var preferenceApi: PreferenceApi? = null
}
