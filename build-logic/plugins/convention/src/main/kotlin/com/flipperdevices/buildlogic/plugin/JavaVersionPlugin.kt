package com.flipperdevices.buildlogic.plugin

import com.android.build.gradle.BaseExtension
import gradle.kotlin.dsl.accessors._27dd010b3a0c01ca94ead647e9e56578.ext
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.findByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.HasConfigurableKotlinCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * Set javaSource, javaTarget and kotlinJvmTarget versions
 */
class JavaVersionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.afterEvaluate {
            target.tasks.withType<JavaCompile> {
                options.encoding = "UTF-8"
            }

            target.extensions.findByType<JavaPluginExtension>()?.run {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
            }

            target.extensions.findByType<BaseExtension>()?.run {
                compileOptions.sourceCompatibility = JavaVersion.VERSION_17
                compileOptions.targetCompatibility = JavaVersion.VERSION_17
            }

            target.extensions.findByType<KotlinMultiplatformExtension>()?.run {
                targets.filterIsInstance<HasConfigurableKotlinCompilerOptions<*>>()
                    .mapNotNull { it.compilerOptions as? KotlinJvmCompilerOptions }
                    .onEach { androidTarget ->
                        androidTarget.jvmTarget.set(JvmTarget.JVM_17)
                    }
            }

            target.tasks
                .withType<KotlinCompile>()
                .configureEach {
                    compilerOptions.jvmTarget.set(JvmTarget.JVM_17)
                }
        }


    }
}
