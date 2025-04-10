plugins {
    `kotlin-dsl`
    id("java-gradle-plugin")
}

dependencies {
    implementation(libs.android.gradle)
    implementation(libs.detekt.gradle)
    implementation(libs.kotlin.gradle)
    implementation(libs.kotlin.ksp.gradle)
    implementation(libs.compose.multiplatform.gradle)
    implementation(libs.compose.gradle)
    implementation(libs.sentry.gradle)

    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}

gradlePlugin {
    plugins {
        create("flipper.multiplatform-dependencies") {
            id = name
            implementationClass =
                "com.flipperdevices.buildlogic.plugin.FlipperMultiplatformDependenciesPlugin"
        }
        create("flipper.java.version") {
            id = name
            implementationClass =
                "com.flipperdevices.buildlogic.plugin.JavaVersionPlugin"
        }
        create("flipper.rebuild.composeres") {
            id = name
            implementationClass =
                "com.flipperdevices.buildlogic.plugin.BuildComposeResourcesPlugin"
        }
        create("flipper.gservices") {
            id = name
            implementationClass =
                "com.flipperdevices.buildlogic.plugin.gservices.GServicesFilePlugin"
        }
    }
}
