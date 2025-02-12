plugins {
    id("flipper.multiplatform-compose")
    id("flipper.multiplatform-dependencies")
    id("flipper.anvil-multiplatform")
    id("kotlinx-serialization")
}

androidDependencies {
    implementation(projects.components.bsb.auth.within.passkey.api)

    implementation(projects.components.core.di)
    implementation(projects.components.core.log)
    implementation(projects.components.core.ktx)
    implementation(projects.components.core.activityholder)
    implementation(projects.components.core.ui.lifecycle)
    implementation(projects.components.core.ui.decompose)
    implementation(projects.components.bsb.core.theme)

    implementation(projects.components.bsb.auth.within.main.api)
    implementation(projects.components.bsb.auth.within.common)

    implementation(projects.components.bsb.cloud.api)

    implementation(libs.decompose)

    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play)
    implementation(libs.identity)

    implementation(libs.kotlin.serialization.json)
}
