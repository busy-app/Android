plugins {
    id("flipper.multiplatform-compose")
    id("flipper.anvil-multiplatform")
    id("kotlinx-serialization")
    id("flipper.multiplatform-dependencies")
}

commonDependencies {
    implementation(projects.components.bsb.auth.signup.api)

    implementation(projects.components.core.di)
    implementation(projects.components.core.ui.decompose)
    implementation(projects.components.bsb.core.theme)

    implementation(projects.components.bsb.auth.common)
    implementation(projects.components.bsb.auth.otp.screen.api)
    implementation(projects.components.bsb.auth.confirmpassword.api)

    implementation(projects.components.bsb.deeplink.api)

    implementation(libs.decompose)
}
