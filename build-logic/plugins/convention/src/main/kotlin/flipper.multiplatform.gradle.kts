import com.android.build.gradle.BaseExtension
import com.flipperdevices.buildlogic.ApkConfig.DISABLE_NATIVE
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

    if (!DISABLE_NATIVE) {
        sourceSets.create("iosMain").dependsOn(sourceSets.commonMain.get())
        sourceSets.create("iosX64Main").dependsOn(sourceSets.commonMain.get())
        sourceSets.create("iosArm64Main").dependsOn(sourceSets.commonMain.get())
        sourceSets.create("iosSimulatorArm64Main").dependsOn(sourceSets.commonMain.get())
    }

    applyDefaultHierarchyTemplate {
        common {
            group("jvmShared") {
                withAndroidTarget()
                withJvm()
            }
        }
    }
    if (DISABLE_NATIVE) {
        sourceSets.create("iosMain")
        sourceSets.create("iosX64Main")
        sourceSets.create("iosArm64Main")
        sourceSets.create("iosSimulatorArm64Main")
    }
}

includeCommonKspConfigurationTo("kspAndroid", "kspDesktop")
