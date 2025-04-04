import com.flipperdevices.buildlogic.ApkConfig
import com.flipperdevices.buildlogic.ApkConfig.CURRENT_FLAVOR_TYPE
import com.flipperdevices.buildlogic.ApkConfig.VERSION_NAME
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    id("flipper.android-app")
    id("flipper.multiplatform-dependencies")
    id("flipper.gservices")
}

if (CURRENT_FLAVOR_TYPE.isGoogleFeatureAvailable) {
    apply(plugin = libs.plugins.googleServices.get().pluginId)
}

android {
    namespace = "com.flipperdevices.bsb"
    defaultConfig {
        targetSdk = ApkConfig.TARGET_SDK_ANDROID_VERSION
    }
}

kotlin {
    androidTarget()
    jvm("desktop")

    val xcFramework = XCFramework()
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true

            xcFramework.add(this)

            export(libs.decompose)
            export(libs.essenty.lifecycle)
            export(libs.settings)
            // TODO revert back export(projects.components.bsb.appblocker.api)
        }
    }

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.timber)

            implementation(projects.components.core.activityholder)
            implementation(libs.appcompat)
            implementation(libs.androidx.splashscreen)

            implementation(projects.components.bsb.wear.bridge.syncservice.api)

            if (CURRENT_FLAVOR_TYPE.isGoogleFeatureAvailable) {
                implementation(projects.components.bsb.wear.bridge.messenger.impl)
                implementation(projects.components.bsb.wear.bridge.messenger.common)
                implementation(projects.components.bsb.wear.bridge.syncservice.android)
                implementation(libs.google.horologist.datalayer)
                implementation(libs.google.horologist.datalayer.phone)
                implementation(projects.components.bsb.timer.syncservice.firebase)
            } else {
                implementation(projects.components.bsb.wear.bridge.syncservice.api)
                implementation(projects.components.bsb.wear.bridge.syncservice.noop)
                implementation(projects.components.bsb.timer.syncservice.noop)
            }
        }
        commonMain.dependencies {
            implementation(projects.components.core.focusDisplay)

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            implementation(libs.settings)

            implementation(libs.kotlin.inject.runtime)
            implementation(libs.kotlin.inject.anvil.runtime)
            implementation(libs.kotlin.inject.anvil.runtime.optional)

            implementation(libs.ktor.client.core)
            implementation(libs.decompose)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.decompose.composeExtension)

            implementation(libs.kotlin.coroutines.swing)

            implementation(projects.components.core.ui.timeline)
            implementation(projects.components.bsb.analytics.metric.noop)
            implementation(projects.components.bsb.timer.syncservice.socket)
        }
        iosMain.dependencies {
            api(libs.decompose)
            api(libs.essenty.lifecycle)
            api(libs.settings)

            // TODO revert back api(projects.components.bsb.appblocker.api)
            implementation(libs.settings.observable)
            implementation(projects.components.bsb.analytics.metric.noop)
            implementation(projects.components.bsb.timer.syncservice.noop)
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.flipperdevices.bsb.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Busy Bar"
            packageVersion = VERSION_NAME
            vendor = "Flipper Devices Inc"

            macOS {
                dockName = "Busy Bar"
                iconFile.set(project.file("icons/icon.icns"))
            }
            windows {
                iconFile.set(project.file("icons/icon.ico"))
            }
            linux {
                iconFile.set(project.file("icons/icon.png"))
            }
        }
    }
}

commonDependencies {
    implementation(projects.components.core.di)
    implementation(projects.components.core.log)
    implementation(projects.components.core.ktx)
    implementation(projects.components.core.apppackage)
    implementation(projects.components.core.buildKonfig)
    implementation(projects.components.core.vibrator)
    implementation(projects.components.core.ui.decompose)
    implementation(projects.components.core.ui.lifecycle)
    implementation(projects.components.bsb.core.theme)

    implementation(projects.components.bsb.analytics.metric.api)
    implementation(projects.components.bsb.timer.syncservice.api)
    if (CURRENT_FLAVOR_TYPE.isGoogleFeatureAvailable) {
        implementation(projects.components.bsb.analytics.metric.firebase)
    } else {
        implementation(projects.components.bsb.analytics.metric.noop)
    }

    implementation(projects.components.bsb.analytics.timer.api)
    implementation(projects.components.bsb.analytics.timer.impl)
    implementation(projects.components.bsb.analytics.shake2report.api)
    implementation(projects.components.bsb.analytics.shake2report.impl)

    implementation(projects.components.bsb.root.api)
    implementation(projects.components.bsb.root.impl)
    implementation(projects.components.bsb.preference.api)
    implementation(projects.components.bsb.preference.impl)
    implementation(projects.components.bsb.cloud.api)
    implementation(projects.components.bsb.cloud.impl)
    implementation(projects.components.bsb.cloudMock)
    implementation(projects.components.bsb.deeplink.api)
    implementation(projects.components.bsb.deeplink.impl)
    implementation(projects.components.bsb.inappnotification.api)
    implementation(projects.components.bsb.inappnotification.impl)
    implementation(projects.components.bsb.sound.api)
    implementation(projects.components.bsb.sound.impl)

    implementation(projects.components.bsb.appblocker.core.api)
    implementation(projects.components.bsb.appblocker.core.impl)
    implementation(projects.components.bsb.appblocker.card.api)
    implementation(projects.components.bsb.appblocker.card.impl)
    implementation(projects.components.bsb.appblocker.filter.api)
    implementation(projects.components.bsb.appblocker.filter.impl)
    implementation(projects.components.bsb.appblocker.permission.api)
    implementation(projects.components.bsb.appblocker.permission.impl)
    implementation(projects.components.bsb.appblocker.screen)
    implementation(projects.components.bsb.appblocker.stats.api)
    implementation(projects.components.bsb.appblocker.stats.impl)

    implementation(projects.components.bsb.dao.api)
    implementation(projects.components.bsb.dao.impl)

    implementation(projects.components.bsb.auth.main.api)
    implementation(projects.components.bsb.auth.main.impl)
    implementation(projects.components.bsb.auth.login.api)
    implementation(projects.components.bsb.auth.login.impl)
    implementation(projects.components.bsb.auth.signup.api)
    implementation(projects.components.bsb.auth.signup.impl)
    implementation(projects.components.bsb.auth.confirmpassword.api)
    implementation(projects.components.bsb.auth.confirmpassword.impl)
    implementation(projects.components.bsb.auth.otp.element.api)
    implementation(projects.components.bsb.auth.otp.element.impl)
    implementation(projects.components.bsb.auth.otp.screen.api)
    implementation(projects.components.bsb.auth.otp.screen.impl)
    implementation(projects.components.bsb.auth.within.main.api)
    implementation(projects.components.bsb.auth.within.main.impl)
    implementation(projects.components.bsb.auth.within.oauth.api)
    implementation(projects.components.bsb.auth.within.oauth.impl)
    implementation(projects.components.bsb.auth.within.onetap.api)
    implementation(projects.components.bsb.auth.within.onetap.impl)
    implementation(projects.components.bsb.auth.within.passkey.api)
    implementation(projects.components.bsb.auth.within.passkey.impl)

    implementation(projects.components.bsb.profile.main.api)
    implementation(projects.components.bsb.profile.main.impl)
    implementation(projects.components.bsb.profile.passkeyview.api)
    implementation(projects.components.bsb.profile.passkeyview.impl)

    implementation(projects.components.bsb.timer.common)
    implementation(projects.components.bsb.timer.setup.api)
    implementation(projects.components.bsb.timer.setup.impl)
    implementation(projects.components.bsb.timer.main.api)
    implementation(projects.components.bsb.timer.main.impl)
    implementation(projects.components.bsb.timer.background.api)
    implementation(projects.components.bsb.timer.background.impl)
    implementation(projects.components.bsb.timer.active.api)
    implementation(projects.components.bsb.timer.active.impl)
    implementation(projects.components.bsb.timer.delayedStart.api)
    implementation(projects.components.bsb.timer.delayedStart.impl)
    implementation(projects.components.bsb.timer.done.api)
    implementation(projects.components.bsb.timer.done.impl)
    implementation(projects.components.bsb.timer.rest.api)
    implementation(projects.components.bsb.timer.rest.impl)
    implementation(projects.components.bsb.timer.cards.api)
    implementation(projects.components.bsb.timer.cards.impl)
    implementation(projects.components.bsb.timer.stateBuilder)
}

dependencies {
    if (CURRENT_FLAVOR_TYPE.isGoogleFeatureAvailable) {
        implementation(platform(libs.firebase.bom))
    }

    add("kspCommonMainMetadata", libs.kotlin.inject.ksp)
    add("kspAndroid", libs.kotlin.inject.ksp)
    add("kspIosArm64", libs.kotlin.inject.ksp)
    add("kspIosX64", libs.kotlin.inject.ksp)
    add("kspIosSimulatorArm64", libs.kotlin.inject.ksp)
    add("kspDesktop", libs.kotlin.inject.ksp)

    add("kspCommonMainMetadata", libs.kotlin.inject.anvil.ksp)
    add("kspAndroid", libs.kotlin.inject.anvil.ksp)
    add("kspIosArm64", libs.kotlin.inject.anvil.ksp)
    add("kspIosX64", libs.kotlin.inject.anvil.ksp)
    add("kspIosSimulatorArm64", libs.kotlin.inject.anvil.ksp)
    add("kspDesktop", libs.kotlin.inject.anvil.ksp)
}
