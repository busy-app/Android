plugins {
    id("flipper.multiplatform-compose")
    id("flipper.anvil-multiplatform")
    id("kotlinx-serialization")
    id("flipper.multiplatform-dependencies")
}

commonDependencies {
    implementation(projects.components.bsb.profile.main.api)

    implementation(projects.components.core.di)
    implementation(projects.components.core.ui.decompose)
    implementation(projects.components.core.ktx)
    implementation(projects.components.bsb.core.theme)

    implementation(projects.components.bsb.deeplink.api)
    implementation(projects.components.bsb.auth.main.api)
    implementation(projects.components.bsb.preference.api)
    implementation(projects.components.bsb.cloud.api)

    implementation(projects.components.bsb.profile.passkeyview.api)

    implementation(libs.decompose)
}
