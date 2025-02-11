plugins {
    id("flipper.multiplatform")
    id("flipper.multiplatform-dependencies")
    id("flipper.anvil-multiplatform")
    id("kotlinx-serialization")
}

commonDependencies {
    implementation(projects.components.bsb.timer.background.api)
    implementation(projects.components.bsb.metronome.api)
    implementation(projects.components.bsb.liveactivity.api)

    implementation(projects.components.core.di)
    implementation(projects.components.core.ktx)
    implementation(projects.components.core.log)
    implementation(projects.components.core.data)

    implementation(libs.kotlin.coroutines)
    implementation(libs.kotlin.datetime)
    implementation(libs.kotlin.immutable)
}

androidDependencies {
    implementation(libs.androidx.core)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle)
    implementation(libs.kotlin.serialization.json)
}

commonTestDependencies {
    implementation(libs.kotlin.test)
}
