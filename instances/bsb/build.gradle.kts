import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import com.flipperdevices.buildlogic.ApkConfig.VERSION_NAME

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    id("flipper.android-app")
    id("flipper.multiplatform-dependencies")
}

android.namespace = "com.flipperdevices.bsb"

kotlin {
    wasmJs {
        moduleName = "bsb"
        browser {
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(projectDirPath)
                    }
                }
            }
        }
        binaries.executable()
    }

    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    jvm("desktop")

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true

            export(libs.decompose)
            export(libs.essenty.lifecycle)
            export(libs.settings)
        }
    }

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.timber)

            implementation(projects.components.core.activityholder)
        }
        commonMain.dependencies {
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
        }
        iosMain.dependencies {
            api(libs.decompose)
            api(libs.essenty.lifecycle)
            api(libs.settings)

            implementation(libs.settings.observable)
        }
        wasmJsMain.dependencies {
            implementation(libs.settings.observable)
            implementation(libs.kotlin.serialization.json)
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
    implementation(projects.components.core.ui.decompose)
    implementation(projects.components.core.ui.lifecycle)
    implementation(projects.components.bsb.core.theme)

    implementation(projects.components.bsb.root.api)
    implementation(projects.components.bsb.root.impl)
    implementation(projects.components.bsb.preference.api)
    implementation(projects.components.bsb.preference.impl)
    implementation(projects.components.bsb.cloud.api)
    implementation(projects.components.bsb.cloud.impl)

    implementation(projects.components.bsb.auth.main.api)
    implementation(projects.components.bsb.auth.main.impl)
    implementation(projects.components.bsb.auth.login.api)
    implementation(projects.components.bsb.auth.login.impl)
    implementation(projects.components.bsb.auth.signup.api)
    implementation(projects.components.bsb.auth.signup.impl)
}

dependencies {
    add("kspCommonMainMetadata", libs.kotlin.inject.ksp)
    add("kspAndroid", libs.kotlin.inject.ksp)
    add("kspIosArm64", libs.kotlin.inject.ksp)
    add("kspIosX64", libs.kotlin.inject.ksp)
    add("kspIosSimulatorArm64", libs.kotlin.inject.ksp)
    add("kspDesktop", libs.kotlin.inject.ksp)
    add("kspWasmJs", libs.kotlin.inject.ksp)

    add("kspCommonMainMetadata", libs.kotlin.inject.anvil.ksp)
    add("kspAndroid", libs.kotlin.inject.anvil.ksp)
    add("kspIosArm64", libs.kotlin.inject.anvil.ksp)
    add("kspIosX64", libs.kotlin.inject.anvil.ksp)
    add("kspIosSimulatorArm64", libs.kotlin.inject.anvil.ksp)
    add("kspDesktop", libs.kotlin.inject.anvil.ksp)
    add("kspWasmJs", libs.kotlin.inject.anvil.ksp)
}
