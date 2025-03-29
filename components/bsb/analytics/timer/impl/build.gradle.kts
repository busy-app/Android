plugins {
    id("flipper.multiplatform")
    id("flipper.anvil-multiplatform")
    id("flipper.multiplatform-dependencies")
}

commonDependencies {
    implementation(projects.components.bsb.analytics.timer.api)
    implementation(projects.components.core.di)

    implementation(projects.components.bsb.dao.api)
    implementation(projects.components.bsb.analytics.metric.api)

    implementation(projects.components.bsb.timer.background.api)

    implementation(libs.kotlin.coroutines)
    implementation(libs.kotlin.datetime)
}
