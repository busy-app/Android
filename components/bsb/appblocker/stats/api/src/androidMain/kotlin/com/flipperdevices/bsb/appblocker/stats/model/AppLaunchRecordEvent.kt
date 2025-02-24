package com.flipperdevices.bsb.appblocker.stats.model

data class AppLaunchRecordEvent(
    val appPackage: String,
    val timestamp: Long
)
