import com.android.build.gradle.BaseExtension
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.library")
    id("flipper.lint")
}

configure<BaseExtension> {
    commonAndroid(project)
}

android.namespace = "com.flipperdevices.${
    project.path
        .removePrefix(":components:")
        .replace(":", ".")
        .replace("-", "")
}"

@OptIn(ExperimentalKotlinGradlePluginApi::class)
kotlin {
    androidTarget()
    jvm("desktop")

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    applyDefaultHierarchyTemplate {
        common {
            group("jvmShared") {
                withAndroidTarget()
                withJvm()
            }
        }
    }
}

includeCommonKspConfigurationTo("kspAndroid", "kspDesktop")
