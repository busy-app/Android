plugins {
    id("flipper.multiplatform")
    id("flipper.multiplatform-dependencies")
    id("flipper.anvil-multiplatform")
}

commonDependencies {
    implementation(projects.components.core.ktx)
    implementation(projects.components.core.log)
    implementation(projects.components.core.di)
    implementation(projects.components.core.vibrator)
    implementation(projects.components.core.activityholder)

    implementation(projects.components.bsb.dao.api)
    implementation(projects.components.bsb.timer.background.api)

    implementation(libs.kotlin.coroutines)
}