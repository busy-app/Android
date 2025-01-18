plugins {
    id("flipper.multiplatform-compose")
    id("flipper.anvil-multiplatform")
    id("flipper.multiplatform-dependencies")
}

commonDependencies {
    implementation(projects.components.bsb.preferencescreen.api)

    implementation(projects.components.core.ktx)
    implementation(projects.components.core.di)
    implementation(projects.components.core.ui.decompose)
    implementation(projects.components.core.ui.lifecycle)
    implementation(projects.components.core.buildKonfig)
    implementation(projects.components.bsb.core.res)
    implementation(projects.components.bsb.core.theme)

    implementation(projects.components.bsb.preference.api)
    implementation(projects.components.bsb.dnd.api)
    implementation(projects.components.bsb.appblocker.api)
    implementation(projects.components.bsb.root.api)
    implementation(projects.components.bsb.cloud.api)
    implementation(projects.components.bsb.metronome.api)

    implementation(projects.components.bsb.deeplink.api)

    implementation(libs.decompose)
}
