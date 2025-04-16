import com.flipperdevices.buildlogic.ApkConfig.DISABLE_NATIVE
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("flipper.lint")
}

@OptIn(ExperimentalKotlinGradlePluginApi::class)
kotlin {
    jvm()

    if (!DISABLE_NATIVE) {
        iosX64()
        iosArm64()
        iosSimulatorArm64()
    }

    applyDefaultHierarchyTemplate {
        common {
        }
    }
    if (DISABLE_NATIVE) {
        sourceSets.create("iosMain").dependsOn(sourceSets.commonMain.get())
        sourceSets.create("iosX64Main").dependsOn(sourceSets.commonMain.get())
        sourceSets.create("iosArm64Main").dependsOn(sourceSets.commonMain.get())
        sourceSets.create("iosSimulatorArm64Main").dependsOn(sourceSets.commonMain.get())
    }
}

includeCommonKspConfigurationTo("kspJvm")
