import com.flipperdevices.buildlogic.ApkConfig.VERSION_NAME
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    id("flipper.android-app")
    id("flipper.multiplatform-dependencies")
}

android.namespace = "com.flipperdevices.videotest"

kotlin {
    wasmJs {
        moduleName = "videotest"
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
//            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    jvm("desktop")

    val xcFramework = XCFramework()
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "VideoTest"
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

            implementation(libs.kotlin.coroutines.swing)

            implementation(projects.components.core.ui.timeline)
        }
        iosMain.dependencies {
            api(libs.decompose)
            api(libs.essenty.lifecycle)
            api(libs.settings)
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
            packageName = "Video Test"
            packageVersion = VERSION_NAME
            vendor = "Flipper Devices Inc"
        }
    }
}

commonDependencies {
    implementation(projects.components.core.ui.video)
    implementation(projects.components.bsb.core.theme)
}