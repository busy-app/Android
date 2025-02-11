plugins {
    id("flipper.multiplatform-compose")
    id("flipper.multiplatform-dependencies")
}

commonDependencies {
    implementation(libs.decompose)
    implementation(libs.decompose.composeExtension)
    implementation(libs.essenty.lifecycle.coroutines)
    implementation(libs.kotlin.serialization.json)
}

androidDependencies {
    implementation(projects.components.core.activityholder)

    implementation(libs.androidx.activity.compose)
}
