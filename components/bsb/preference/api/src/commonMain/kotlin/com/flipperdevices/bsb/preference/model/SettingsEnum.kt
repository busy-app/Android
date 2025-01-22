package com.flipperdevices.bsb.preference.model

enum class SettingsEnum(val key: String) {
    DARK_THEME("dark_theme"),
    USER_DATA("user_data"),
    AUTH_TOKEN("token"),
    DND_SUPPORT("dnd_support"),
    APP_BLOCKING("app_blocking"),
    METRONOME("metronome"),
    DEV_MODE("dev_mode"),

    // Code verifier for auth PKCE:
    // https://auth0.com/docs/get-started/authentication-and-authorization-flow/authorization-code-flow-with-pkce/call-your-api-using-the-authorization-code-flow-with-pkce#authorize-user
    AUTH_PENDING_CODE_VERIFIER("auth_pending_code_verifier")
}
