package com.flipperdevices.buildlogic.plugin.svgresource

import com.flipperdevices.buildlogic.util.ProjectExt.kotlin
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Set javaSource, javaTarget and kotlinJvmTarget versions
 */
class SvgToXmlPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.tasks.register("convertSvgToXmlResources") {
            target.tasks.filter { it.name.contains("generateComposeResClass") }
                .onEach { task ->
                    task.dependsOn(this)
                    task.mustRunAfter(this)
                    task.shouldRunAfter(this)
                    this.finalizedBy(task)
                }
            target.tasks.filter { it.name.contains("generateResourceAccessors") }
                .onEach { task ->
                    task.dependsOn(this)
                    task.mustRunAfter(this)
                    task.shouldRunAfter(this)
                    this.finalizedBy(task)
                }

            val commonMainSourceSet = target.kotlin.sourceSets
                .findByName("commonMain")
                ?: throw GradleException("SvgToXmlPlugin is applied, but no commonMain source set found!")
            AndroidImageResourceGenerator(
                sourceSetDir = commonMainSourceSet
                    .project
                    .file("src")
                    .resolve("commonMain"),
                logger = target.logger,
            ).generateResourceFiles()
        }
    }
}
