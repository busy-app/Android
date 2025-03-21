plugins {
    id("flipper.multiplatform")
    id("flipper.multiplatform-dependencies")
    alias(libs.plugins.kotlinSerialization)
}

commonDependencies {
    implementation(libs.kotlin.serialization.json)
}