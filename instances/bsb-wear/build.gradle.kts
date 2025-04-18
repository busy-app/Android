import com.flipperdevices.buildlogic.ApkConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinSerialization)
    id("flipper.android-app")
    id("flipper.multiplatform-dependencies")
}

android {
    namespace = "com.flipperdevices.bsbwearable"
    defaultConfig {
        targetSdk = ApkConfig.TARGET_SDK_WEAROS_VERSION
    }
}

kotlin {
    androidTarget()
}

commonDependencies {

    implementation(projects.components.core.di)
    implementation(projects.components.core.log)
    implementation(projects.components.core.ktx)
    implementation(projects.components.core.focusDisplay)
    implementation(projects.components.core.vibrator)
    implementation(projects.components.core.ui.decompose)
    implementation(projects.components.core.ui.lifecycle)
    implementation(projects.components.core.ui.button)
    implementation(projects.components.core.ui.cardFrame)
    implementation(projects.components.core.ui.picker)
    implementation(projects.components.core.ui.timeline)
    implementation(projects.components.core.ui.text)
    implementation(projects.components.core.ui.autosizetext)
    implementation(projects.components.core.activityholder)
    implementation(projects.components.core.trustedClock.api)
    implementation(projects.components.bsb.deeplink.api)
    implementation(projects.components.bsb.deeplink.impl)
    implementation(projects.components.bsb.core.theme)
    implementation(projects.components.bsb.core.res)
    implementation(projects.components.bsb.timer.background.api)
    implementation(projects.components.bsb.timer.stateBuilder)
    implementation(projects.components.bsb.timer.background.impl)
    implementation(projects.components.bsb.timer.notification.api)
    implementation(projects.components.bsb.timer.notification.common)
    implementation(projects.components.bsb.timer.notification.wearos)
    implementation(projects.components.bsb.timer.common)
    implementation(projects.components.bsb.timer.controller)
    implementation(projects.components.bsb.preference.api)
    implementation(projects.components.bsb.dao.api)
    implementation(projects.components.bsb.wear.bridge.messenger.common)
    implementation(projects.components.bsb.wear.bridge.messenger.impl)
    implementation(projects.components.bsb.wear.bridge.syncservice.wear)

    implementation(libs.settings)
    implementation(libs.settings.coroutines)
    implementation(libs.timber)

    implementation(libs.kotlin.inject.runtime)
    implementation(libs.kotlin.inject.anvil.runtime)
    implementation(libs.kotlin.inject.anvil.runtime.optional)

    implementation(kotlin.compose.runtime)
    implementation(kotlin.compose.foundation)
    implementation(kotlin.compose.material)
    implementation(kotlin.compose.ui)
    implementation(kotlin.compose.components.resources)
    implementation(kotlin.compose.components.uiToolingPreview)

    implementation(libs.compose.haze)
    implementation(libs.compose.haze.materials)

    implementation(libs.klibs.kstorage)
    implementation(libs.kotlin.serialization.json)
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.timber)
    implementation(libs.wear)
    implementation(libs.wear.gms)
    implementation(libs.constraintlayout)
    implementation(compose.preview)
    implementation(libs.compose.wear.preview)

    implementation(libs.androidx.splashscreen)
    implementation(libs.androidx.activity.compose)
    implementation(libs.compose.wear.foundation)
    implementation(libs.compose.wear.material)
    implementation(libs.horologist.layout)
    implementation(libs.horologist.material)
    implementation(libs.decompose)
    implementation(libs.essenty.lifecycle.coroutines)
    implementation(libs.decompose.composeExtension)
    implementation(libs.kotlin.immutable)
    implementation(libs.kotlin.datetime)

    implementation(libs.google.horologist.datalayer)
    implementation(libs.google.horologist.datalayer.watch)

    implementation(projects.components.core.trustedClock.truetime)
}

dependencies {
    add("kspCommonMainMetadata", libs.kotlin.inject.ksp)
    add("kspAndroid", libs.kotlin.inject.ksp)

    add("kspCommonMainMetadata", libs.kotlin.inject.anvil.ksp)
    add("kspAndroid", libs.kotlin.inject.anvil.ksp)
}
