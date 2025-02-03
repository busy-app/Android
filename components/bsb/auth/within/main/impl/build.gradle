plugins {
    id("flipper.multiplatform-compose")
    id("flipper.anvil-multiplatform")
    id("kotlinx-serialization")
    id("flipper.multiplatform-dependencies")
}

commonDependencies {
    implementation(projects.components.bsb.auth.within.main.api)

    implementation(projects.components.core.di)
    implementation(projects.components.core.ui.decompose)

    implementation(projects.components.bsb.deeplink.api)
    implementation(projects.components.bsb.auth.within.oauth.data)

    implementation(libs.decompose)
}

androidDependencies {
    implementation(projects.components.bsb.core.theme)

    implementation(projects.components.bsb.auth.within.oauth.api)
    implementation(projects.components.bsb.auth.within.onetap.api)
}
