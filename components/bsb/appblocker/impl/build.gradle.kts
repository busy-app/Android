plugins {
    id("flipper.multiplatform")
    id("flipper.multiplatform-dependencies")
    id("flipper.anvil-multiplatform")
}

commonDependencies {
    implementation(projects.components.bsb.appblocker.api)
    implementation(projects.components.bsb.preference.api)
    implementation(projects.components.core.di)
}

androidDependencies {
    implementation(projects.components.core.ktx)
    implementation(projects.components.core.log)
    implementation(projects.components.core.activityholder)

    implementation(projects.components.bsb.timer.background.api)
    implementation(projects.components.bsb.deeplink.api)

    implementation(libs.kotlin.coroutines)
    implementation(libs.kotlin.datetime)
}

iosDependencies {
    implementation(projects.components.bsb.timer.background.api)

    implementation(projects.components.core.log)

    implementation(libs.kotlin.coroutines)
    implementation(libs.kotlin.datetime)
}
