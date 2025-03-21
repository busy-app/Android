import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("flipper.lint")
}

@OptIn(ExperimentalKotlinGradlePluginApi::class)
kotlin {
    jvm()

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    applyDefaultHierarchyTemplate {
        common {
        }
    }
}

includeCommonKspConfigurationTo("kspJvm")
