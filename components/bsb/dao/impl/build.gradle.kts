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
    implementation(projects.components.core.log)
    implementation(projects.components.core.ktx)

    implementation(projects.components.bsb.preference.api)
    implementation(projects.components.bsb.appblocker.permission.api)

    implementation(libs.room.runtime)
    implementation(libs.kotlin.serialization.json)
}

dependencies {
    ksp(libs.room.compiler)
}
