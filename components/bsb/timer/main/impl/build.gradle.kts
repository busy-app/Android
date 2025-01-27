plugins {
    id("flipper.multiplatform-compose")
    id("flipper.anvil-multiplatform")
    id("kotlinx-serialization")
    id("flipper.multiplatform-dependencies")
}

commonDependencies {
    implementation(projects.components.bsb.timer.main.api)

    implementation(projects.components.core.di)
    implementation(projects.components.core.log)
    implementation(projects.components.core.ktx)
    implementation(projects.components.core.data)
    implementation(projects.components.bsb.core.theme)
    implementation(projects.components.core.ui.decompose)
    implementation(projects.components.core.ui.lifecycle)

    implementation(projects.components.bsb.root.api)
    implementation(projects.components.bsb.preference.api)
    implementation(projects.components.bsb.timer.setup.api)
    implementation(projects.components.bsb.timer.active.api)
    implementation(projects.components.bsb.timer.common)
    implementation(projects.components.bsb.timer.background.api)

    implementation(libs.decompose)
    implementation(libs.essenty.lifecycle.coroutines)
}
