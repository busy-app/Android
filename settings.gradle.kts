rootProject.name = "BusyStatusBar"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")

    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

include(
    ":instances:bsb",
    ":instances:bsb-wear",
    ":instances:videotest",

    ":components:core:di",
    ":components:core:activityholder",
    ":components:core:log",
    ":components:core:ktx",
    ":components:core:data",
    ":components:core:build-konfig",
    ":components:core:focus-display",
    ":components:core:ui:decompose",
    ":components:core:ui:lifecycle",
    ":components:core:ui:appbar",
    ":components:core:ui:button",
    ":components:core:ui:sheet",
    ":components:core:ui:picker",
    ":components:core:ui:option",
    ":components:core:ui:timeline",
    ":components:core:ui:card-frame",
    ":components:core:ui:res-preview",
    ":components:core:ui:video",

    ":components:bsb:core:theme",
    ":components:bsb:core:res",
    ":components:bsb:core:markdown",
    ":components:bsb:core:files",

    ":components:bsb:root:api",
    ":components:bsb:root:impl",
    ":components:bsb:preference:api",
    ":components:bsb:preference:impl",
    ":components:bsb:cloud:api",
    ":components:bsb:cloud:impl",
    ":components:bsb:dnd:api",
    ":components:bsb:dnd:impl",
    ":components:bsb:deeplink:api",
    ":components:bsb:deeplink:impl",
    ":components:bsb:inappnotification:api",
    ":components:bsb:inappnotification:impl",
    ":components:bsb:metronome:api",
    ":components:bsb:metronome:impl",
    ":components:bsb:liveactivity:api",

    ":components:bsb:appblocker:core:api",
    ":components:bsb:appblocker:core:impl",
    ":components:bsb:appblocker:card:api",
    ":components:bsb:appblocker:card:impl",
    ":components:bsb:appblocker:filter:api",
    ":components:bsb:appblocker:filter:impl",
    ":components:bsb:appblocker:permission:api",
    ":components:bsb:appblocker:permission:impl",
    ":components:bsb:appblocker:screen:api",
    ":components:bsb:appblocker:screen:impl",
    ":components:bsb:appblocker:stats:api",
    ":components:bsb:appblocker:stats:impl",

    ":components:bsb:auth:common",
    ":components:bsb:auth:main:impl",
    ":components:bsb:auth:main:api",
    ":components:bsb:auth:main:impl",
    ":components:bsb:auth:login:api",
    ":components:bsb:auth:login:impl",
    ":components:bsb:auth:signup:api",
    ":components:bsb:auth:signup:impl",
    ":components:bsb:auth:confirmpassword:api",
    ":components:bsb:auth:confirmpassword:impl",
    ":components:bsb:auth:otp:element:api",
    ":components:bsb:auth:otp:element:impl",
    ":components:bsb:auth:otp:screen:api",
    ":components:bsb:auth:otp:screen:impl",
    ":components:bsb:auth:within:common",
    ":components:bsb:auth:within:main:api",
    ":components:bsb:auth:within:main:impl",
    ":components:bsb:auth:within:oauth:api",
    ":components:bsb:auth:within:oauth:data",
    ":components:bsb:auth:within:oauth:impl",
    ":components:bsb:auth:within:onetap:api",
    ":components:bsb:auth:within:onetap:impl",
    ":components:bsb:auth:within:passkey:api",
    ":components:bsb:auth:within:passkey:impl",

    ":components:bsb:profile:common",
    ":components:bsb:profile:main:api",
    ":components:bsb:profile:main:impl",
    ":components:bsb:profile:passkeyview:api",
    ":components:bsb:profile:passkeyview:impl",

    ":components:bsb:timer:common",
    ":components:bsb:timer:setup:api",
    ":components:bsb:timer:setup:impl",
    ":components:bsb:timer:main:api",
    ":components:bsb:timer:main:impl",
    ":components:bsb:timer:rest:api",
    ":components:bsb:timer:rest:impl",
    ":components:bsb:timer:done:api",
    ":components:bsb:timer:done:impl",
    ":components:bsb:timer:background:api",
    ":components:bsb:timer:background:impl",
    ":components:bsb:timer:active:api",
    ":components:bsb:timer:active:impl",
    ":components:bsb:timer:delayed-start:api",
    ":components:bsb:timer:delayed-start:impl",
    ":components:bsb:timer:cards:api",
    ":components:bsb:timer:cards:impl",
)
