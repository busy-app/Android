plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinSerialization)
    id("flipper.android-app")
    id("flipper.multiplatform-dependencies")
}

android.namespace = "com.flipperdevices.bsbwearable"

kotlin {
    androidTarget()
}
commonDependencies {

    implementation(libs.timber)

    implementation(projects.components.core.di)
    implementation(projects.components.core.log)
    implementation(projects.components.core.ktx)
    implementation(projects.components.core.ui.decompose)
    implementation(projects.components.core.ui.lifecycle)
    implementation(projects.components.core.activityholder)
    implementation(projects.components.bsb.deeplink.api)
    implementation(projects.components.bsb.deeplink.impl)
    implementation(projects.components.bsb.core.theme)
    implementation(projects.components.bsb.core.res)

    implementation(libs.settings)

    implementation(libs.kotlin.inject.runtime)
    implementation(libs.kotlin.inject.anvil.runtime)
    implementation(libs.kotlin.inject.anvil.runtime.optional)

    implementation(projects.components.core.focusDisplay)

    implementation(kotlin.compose.runtime)
    implementation(kotlin.compose.foundation)
    implementation(kotlin.compose.material)
    implementation(kotlin.compose.ui)
    implementation(kotlin.compose.components.resources)
    implementation(kotlin.compose.components.uiToolingPreview)
}
dependencies {

//    implementation(libs.androidx.activity.compose)
    implementation(libs.appcompat)

//    implementation(libs.ktor.client.core)

    // FL

//    implementation(libs.appcompat)
    implementation(libs.timber)
//    implementation(libs.datastore)
//    implementation(libs.splashscreen)

    implementation(libs.wear)
    implementation(libs.wear.gms)

    // Compose
    implementation(libs.androidx.activity.compose)
//    implementation(libs.compose.ui)
//    implementation(libs.compose.tooling)
    implementation(libs.compose.wear.foundation)
    implementation(libs.compose.wear.material)
    implementation(libs.horologist.layout)
    implementation(libs.decompose)
    implementation(libs.decompose.composeExtension)
//    implementation(libs.lifecycle.compose)

    implementation(libs.kotlin.immutable)
//    implementation(libs.kotlin.coroutines.play.services)
}

dependencies {
    add("kspCommonMainMetadata", libs.kotlin.inject.ksp)
    add("kspAndroid", libs.kotlin.inject.ksp)

    add("kspCommonMainMetadata", libs.kotlin.inject.anvil.ksp)
    add("kspAndroid", libs.kotlin.inject.anvil.ksp)
}
