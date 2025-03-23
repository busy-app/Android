plugins {
    id("flipper.multiplatform")
    id("flipper.anvil-multiplatform")
    id("flipper.multiplatform-dependencies")
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

commonDependencies {
    implementation(projects.components.bsb.dao.api)

    implementation(projects.components.core.di)
    implementation(projects.components.core.ktx)

    implementation(libs.room.runtime)
}

dependencies {
    ksp(libs.room.compiler)
}
