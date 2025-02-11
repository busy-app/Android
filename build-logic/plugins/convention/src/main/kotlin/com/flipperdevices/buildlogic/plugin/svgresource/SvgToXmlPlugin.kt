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
            val commonMainSourceSet = target.kotlin.sourceSets
                .findByName("commonMain")
                ?: throw GradleException("SvgToXmlPlugin is applied, but no commonMain source set found!")
            AndroidImageResourceGenerator(
                sourceSetDir = commonMainSourceSet
                    .project
                    .file("src")
                    .resolve("commonMain"),
                logger = logger,
            ).generateResourceFiles()
        }
    }
}
