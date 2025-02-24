plugins {
    id("flipper.multiplatform")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.ui.tooling)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            // For Android Compose Preview on IntelliJ > 2025.1 EAP
            implementation(compose.components.uiToolingPreview)
        }
        val desktopMain by getting
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
    }
}