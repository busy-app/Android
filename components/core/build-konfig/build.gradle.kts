import com.flipperdevices.buildlogic.ApkConfig.CURRENT_FLAVOR_TYPE
import com.flipperdevices.buildlogic.ApkConfig.VERSION_NAME

plugins {
    id("flipper.multiplatform")
    id("flipper.multiplatform-dependencies")
    alias(libs.plugins.buildkonfig)
}

buildConfig {
    className("BuildKonfig")
    packageName("${android.namespace}")
    useKotlinOutput { internalVisibility = false }
    buildConfigField(String::class.java, "PACKAGE", "${android.namespace}")
    buildConfigField(Boolean::class.java, "IS_LOG_ENABLED", CURRENT_FLAVOR_TYPE.isLogEnabled)
    buildConfigField(
        Boolean::class.java,
        "CRASH_APP_ON_FAILED_CHECKS",
        CURRENT_FLAVOR_TYPE.crashAppOnFailedChecks
    )
    buildConfigField(
        String::class.java,
        "VERSION",
        project.VERSION_NAME
    )
}
