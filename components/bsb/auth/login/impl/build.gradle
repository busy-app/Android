plugins {
    id("flipper.multiplatform-compose")
    id("flipper.anvil-multiplatform")
    id("kotlinx-serialization")
    id("flipper.multiplatform-dependencies")
}
dependencies {
    implementation(project(":components:bsb:core:theme"))
}

commonDependencies {
    implementation(projects.components.bsb.auth.login.api)

    implementation(projects.components.core.di)
    implementation(projects.components.core.ktx)
    implementation(projects.components.core.log)
    implementation(projects.components.core.ui.decompose)
    implementation(projects.components.core.ui.lifecycle)
    implementation(projects.components.bsb.core.theme)

    implementation(projects.components.bsb.auth.common)
    implementation(projects.components.bsb.auth.otp.screen.api)
    implementation(projects.components.bsb.auth.confirmpassword.api)
    implementation(projects.components.bsb.cloud.api)
    implementation(projects.components.bsb.preference.api)
    implementation(projects.components.bsb.inappnotification.api)
    implementation(projects.components.bsb.deeplink.api)

    implementation(libs.decompose)
}
