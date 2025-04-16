import com.flipperdevices.buildlogic.ApkConfig.DISABLE_NATIVE

plugins {
    id("flipper.multiplatform")
    id("com.google.devtools.ksp")
}

kotlin {
    sourceSets {
        val commonMain by getting

        commonMain.dependencies {
            implementation(libs.kotlin.inject.runtime)
            implementation(libs.kotlin.inject.anvil.runtime)
            implementation(libs.kotlin.inject.anvil.runtime.optional)
        }
    }
}

dependencies {
    add("kspCommonMainMetadata", libs.kotlin.inject.ksp)
    add("kspAndroid", libs.kotlin.inject.ksp)
    if (!DISABLE_NATIVE) {
        add("kspIosArm64", libs.kotlin.inject.ksp)
        add("kspIosX64", libs.kotlin.inject.ksp)
        add("kspIosSimulatorArm64", libs.kotlin.inject.ksp)
    }
    add("kspDesktop", libs.kotlin.inject.ksp)

    add("kspCommonMainMetadata", libs.kotlin.inject.anvil.ksp)
    add("kspAndroid", libs.kotlin.inject.anvil.ksp)
    if (!DISABLE_NATIVE) {
        add("kspIosArm64", libs.kotlin.inject.anvil.ksp)
        add("kspIosX64", libs.kotlin.inject.anvil.ksp)
        add("kspIosSimulatorArm64", libs.kotlin.inject.anvil.ksp)
    }
    add("kspDesktop", libs.kotlin.inject.anvil.ksp)
}
