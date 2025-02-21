plugins {
    id("flipper.multiplatform-compose")
    id("flipper.anvil-multiplatform")
    id("flipper.multiplatform-dependencies")
}

commonDependencies {
    implementation(projects.components.bsb.appblocker.permission.api)

    implementation(projects.components.core.di)
    implementation(projects.components.core.ktx)
    implementation(projects.components.core.activityholder)
    implementation(projects.components.core.ui.decompose)
    implementation(projects.components.core.ui.lifecycle)
    implementation(projects.components.bsb.core.theme)

    implementation(libs.decompose)
    implementation(libs.essenty.lifecycle.coroutines)
}
