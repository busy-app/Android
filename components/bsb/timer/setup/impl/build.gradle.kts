plugins {
    id("flipper.multiplatform-compose")
    id("flipper.anvil-multiplatform")
    id("flipper.multiplatform-dependencies")
}

commonDependencies {
    implementation(projects.components.bsb.timer.setup.api)

    implementation(projects.components.core.di)
    implementation(projects.components.core.ui.picker)
    implementation(projects.components.core.data)
    implementation(projects.components.bsb.core.theme)
    implementation(projects.components.core.ui.decompose)

    implementation(projects.components.bsb.timer.background.api)

    implementation(libs.decompose)
    implementation(libs.kotlin.datetime)
}
