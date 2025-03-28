plugins {
    id("flipper.anvil-multiplatform")
    id("flipper.multiplatform-dependencies")
}

commonDependencies {
    implementation(projects.components.core.di)
    implementation(projects.components.core.ktx)
}

androidDependencies {
    implementation(libs.appcompat)
}
