package com.flipperdevices.bsb.appblocker.filter.model

data class UIAppInformation(
    val packageName: String,
    val appName: String,
    val category: AppCategory,
    val isBlocked: Boolean
)