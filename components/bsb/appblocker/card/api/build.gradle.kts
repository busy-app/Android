plugins {
    id("flipper.multiplatform-compose")
    id("flipper.multiplatform-dependencies")
}

commonDependencies {
    implementation(libs.decompose)
    implementation(projects.components.core.ui.decompose)
}
