package com.flipperdevices.bsb.preference.model

enum class SettingsEnum(val key: String) {
    USER_DATA("user_data"),
    AUTH_TOKEN("token"),

    // Code verifier for auth PKCE:
    // https://auth0.com/docs/get-started/authentication-and-authorization-flow/authorization-code-flow-with-pkce/call-your-api-using-the-authorization-code-flow-with-pkce#authorize-user
    AUTH_PENDING_CODE_VERIFIER("auth_pending_code_verifier"),

    @Deprecated("Use CardAppBlockerApi instead")
    DEPRECATED_TIMER_SETTINGS("timer_settings"),
    @Deprecated("Use CardAppBlockerApi instead")
    DEPRECATED_APP_BLOCKING("app_blocking"),

    FIREBASE_TOKEN("firebase_token"),
}
