plugins {
    id("flipper.multiplatform-compose")
    id("flipper.multiplatform-dependencies")
    alias(libs.plugins.kotlinSerialization)
    id("flipper.anvil-multiplatform")
}

commonDependencies {
    implementation(projects.components.bsb.cloud.api)

    implementation(projects.components.core.di)
    implementation(projects.components.core.log)
    implementation(projects.components.core.ktx)

    implementation(projects.components.bsb.preference.api)

    implementation(libs.kotlin.datetime)
    implementation(libs.kotlin.serialization.json)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.negotiation)
    implementation(libs.ktor.serialization)
    implementation(libs.ktor.client.logging)
}

androidDependencies {
    implementation(libs.ktor.client.cio)
}

desktopDependencies {
    implementation(libs.ktor.client.cio)
}

iosDependencies {
    implementation(libs.ktor.client.cio)
}

wasmJsDependencies {
    implementation(libs.ktor.client.js)
}
