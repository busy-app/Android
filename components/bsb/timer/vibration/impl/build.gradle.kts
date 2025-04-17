plugins {
    id("flipper.multiplatform")
    id("flipper.multiplatform-dependencies")
}

commonDependencies {
    implementation(projects.components.core.ktx)
    implementation(projects.components.core.log)
    implementation(projects.components.core.vibrator)
    implementation(projects.components.core.activityholder)

    implementation(projects.components.bsb.timer.background.api)
}