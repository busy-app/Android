package com.flipperdevices.buildlogic.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * ./gradlew buildComposeResources to bypass every time sync for resources generation
 */
class BuildComposeResourcesPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.afterEvaluate {
            if (!target.plugins.hasPlugin("org.jetbrains.kotlin.plugin.compose")) return@afterEvaluate
            target.tasks.register("buildComposeResources") {
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
            }
        }
    }
}
