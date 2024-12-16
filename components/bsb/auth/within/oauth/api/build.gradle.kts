plugins {
    id("flipper.multiplatform-compose")
    id("flipper.multiplatform-dependencies")
}

commonDependencies {
    implementation(libs.decompose)
    implementation(projects.components.core.ui.decompose)

    api(projects.components.bsb.auth.within.oauth.data)
}

androidDependencies {
    implementation(projects.components.bsb.auth.within.main.api)
}