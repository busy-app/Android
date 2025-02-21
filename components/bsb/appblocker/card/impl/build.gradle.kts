plugins {
    id("flipper.multiplatform-compose")
    id("flipper.anvil-multiplatform")
    id("flipper.multiplatform-dependencies")
}

commonDependencies {
    implementation(projects.components.bsb.appblocker.card.api)

    implementation(projects.components.core.di)
    implementation(projects.components.core.ktx)
    implementation(projects.components.core.ui.decompose)
    implementation(projects.components.core.ui.button)
    implementation(projects.components.core.ui.option)
    implementation(projects.components.bsb.core.theme)

    implementation(projects.components.bsb.appblocker.core.api)
    implementation(projects.components.bsb.appblocker.permission.api)
    implementation(projects.components.bsb.appblocker.filter.api)

    implementation(libs.kotlin.immutable)
    implementation(libs.decompose)
}
