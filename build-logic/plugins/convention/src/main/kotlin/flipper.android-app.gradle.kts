import com.android.build.gradle.BaseExtension
import com.flipperdevices.buildlogic.ApkConfig
import com.flipperdevices.buildlogic.ApkConfig.CURRENT_FLAVOR_TYPE
import io.sentry.android.gradle.extensions.SentryPluginExtension

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.multiplatform")
    id("flipper.lint")
    id("org.jetbrains.kotlin.plugin.compose")
    id("io.sentry.android.gradle")
}

@Suppress("UnstableApiUsage")
configure<BaseExtension> {
    commonAndroid(project)

    defaultConfig {
        applicationId = ApkConfig.APPLICATION_ID
    }

    buildTypes {
        internal {
            isShrinkResources = true
            isMinifyEnabled = true
            consumerProguardFile(
                "proguard-rules.pro"
            )
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            isShrinkResources = true
            isMinifyEnabled = true
            consumerProguardFile(
                "proguard-rules.pro"
            )
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

configure<SentryPluginExtension> {
    autoUploadProguardMapping.set(CURRENT_FLAVOR_TYPE.isSentryPublishMappingsEnabled)
    telemetry.set(false)

    ignoredBuildTypes.set(setOf("debug"))

    autoInstallation.enabled.set(false)
}

includeCommonKspConfigurationTo("ksp")
