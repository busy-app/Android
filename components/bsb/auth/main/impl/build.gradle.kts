plugins {
    id("flipper.multiplatform-compose")
    id("flipper.anvil-multiplatform")
    id("kotlinx-serialization")
    id("flipper.multiplatform-dependencies")
}

commonDependencies {
    implementation(projects.components.bsb.auth.main.api)

    implementation(projects.components.core.di)
    implementation(projects.components.core.ktx)
    implementation(projects.components.core.log)
    implementation(projects.components.core.ui.decompose)
    implementation(projects.components.core.ui.lifecycle)
    implementation(projects.components.bsb.core.theme)

    implementation(projects.components.bsb.cloud.api)
    implementation(projects.components.bsb.auth.common)

    implementation(libs.decompose)
    implementation(libs.markdown.renderer)
}