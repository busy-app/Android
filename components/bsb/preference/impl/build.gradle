plugins {
    id("flipper.anvil-multiplatform")
    id("flipper.multiplatform-compose")
    id("flipper.multiplatform-dependencies")
    alias(libs.plugins.kotlinSerialization)
}

commonDependencies {
    implementation(projects.components.bsb.preference.api)

    implementation(projects.components.core.di)
    implementation(projects.components.core.ktx)
    implementation(projects.components.bsb.core.theme)
    implementation(projects.components.bsb.core.res)
    implementation(projects.components.core.ui.lifecycle)
    implementation(projects.components.core.ui.decompose)

    implementation(libs.decompose)

    implementation(libs.kotlin.serialization.json)
    implementation(libs.settings)
    implementation(libs.settings.coroutines)
}
