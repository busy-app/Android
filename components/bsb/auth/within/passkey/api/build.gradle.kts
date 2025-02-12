plugins {
    id("flipper.multiplatform")
    id("flipper.multiplatform-dependencies")
}

commonDependencies {
    implementation(libs.decompose)
    implementation(projects.components.core.ui.decompose)

    implementation(projects.components.bsb.auth.within.main.api)
}
