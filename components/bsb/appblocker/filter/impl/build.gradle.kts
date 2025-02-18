plugins {
    id("flipper.multiplatform-compose")
    id("flipper.anvil-multiplatform")
    id("flipper.multiplatform-dependencies")
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

commonDependencies {
    implementation(projects.components.bsb.appblocker.filter.api)

    implementation(projects.components.core.di)
    implementation(projects.components.core.ktx)
    implementation(projects.components.core.ui.decompose)
    implementation(projects.components.core.ui.button)
    implementation(projects.components.core.ui.sheet)
    implementation(projects.components.bsb.core.theme)

    implementation(libs.decompose)
    implementation(libs.composables)

}

androidDependencies {
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
}

dependencies {
    ksp(libs.room.compiler)
}