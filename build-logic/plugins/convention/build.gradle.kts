import org.jetbrains.kotlin.gradle.dsl.JvmTarget

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

    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}

kotlin {
//    jvmToolchain {
//        languageVersion.set(JavaLanguageVersion.of(17))
//    }
    compilerOptions {
//        jvmTarget.set(JvmTarget.JVM_17)
    }
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
    }
}
