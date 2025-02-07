import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("flipper.lint")
}

@OptIn(ExperimentalKotlinGradlePluginApi::class)
kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }
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
