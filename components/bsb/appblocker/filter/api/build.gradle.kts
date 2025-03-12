plugins {
    id("flipper.multiplatform-compose")
    id("flipper.multiplatform-dependencies")
    id("kotlinx-serialization")
}

commonDependencies {
    implementation(libs.decompose)
    implementation(projects.components.core.ui.decompose)

    implementation(libs.kotlin.serialization.json)
}
