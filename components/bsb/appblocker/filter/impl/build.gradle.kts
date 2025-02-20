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
    implementation(projects.components.core.log)
    implementation(projects.components.core.ui.decompose)
    implementation(projects.components.core.ui.lifecycle)
    implementation(projects.components.core.ui.button)
    implementation(projects.components.core.ui.sheet)
    implementation(projects.components.bsb.core.theme)

    implementation(projects.components.bsb.appblocker.core.api)
    implementation(projects.components.bsb.appblocker.permission.api)

    implementation(libs.kotlin.immutable)
    implementation(libs.decompose)
    implementation(libs.composables)
    implementation(kotlin.compose.material3)
}

androidDependencies {
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.accompanist.drawable)
}

dependencies {
    ksp(libs.room.compiler)
}
