package com.flipperdevices.buildlogic.plugin.gservices

import com.flipperdevices.buildlogic.ApkConfig.CURRENT_FLAVOR_TYPE
import com.flipperdevices.buildlogic.model.FlavorType
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project

class GServicesFilePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val gServicesFileName = when (target.CURRENT_FLAVOR_TYPE) {
            FlavorType.DEV -> "google-services-test.json"
            FlavorType.PROD_GP -> "google-services-prod.json"
            FlavorType.PROD_GH_NOGMS, FlavorType.PROD_GH_GMS -> {
                throw GradleException("This plugin can't be applied with this flavor type!")
            }
        }
        val googleServicesFile = target.file(gServicesFileName)
        if (!googleServicesFile.exists()) {
            throw GradleException("Could not find file $googleServicesFile in $target")
        }
        googleServicesFile.copyTo(target.file("google-services.json"))
    }
}
