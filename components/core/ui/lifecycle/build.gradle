plugins {
    id("flipper.multiplatform-compose")
    id("flipper.multiplatform-dependencies")
    alias(libs.plugins.atomicflu)
}

commonDependencies {
    implementation(projects.components.core.ktx)
    implementation(projects.components.core.log)

    api(libs.decompose)
    implementation(libs.kotlin.coroutines)
    api(libs.essenty.lifecycle)
    implementation(libs.essenty.lifecycle.coroutines)

    implementation(libs.androidx.annotation)
    implementation(libs.atomicflu)
}
