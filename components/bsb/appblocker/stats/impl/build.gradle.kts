plugins {
    id("flipper.multiplatform")
    id("flipper.anvil-multiplatform")
    id("flipper.multiplatform-dependencies")
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

commonDependencies {
    implementation(projects.components.bsb.appblocker.stats.api)

    implementation(projects.components.core.di)
    implementation(projects.components.core.log)
    implementation(projects.components.bsb.core.files)

    implementation(libs.kotlin.datetime)
}

androidDependencies {
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.accompanist.drawable)
}

dependencies {
    ksp(libs.room.compiler)
}
