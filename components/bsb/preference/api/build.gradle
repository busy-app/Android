plugins {
    id("flipper.multiplatform-compose")
    id("flipper.multiplatform-dependencies")
    alias(libs.plugins.kotlinSerialization)
}

commonDependencies {
    implementation(libs.decompose)
    implementation(projects.components.core.ui.decompose)
    implementation(libs.kotlin.serialization.json)

    api(libs.klibs.kstorage)
}
