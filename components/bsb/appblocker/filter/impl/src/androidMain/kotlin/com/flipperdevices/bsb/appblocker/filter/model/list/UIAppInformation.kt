package com.flipperdevices.bsb.appblocker.filter.model.list

data class UIAppInformation(
    val packageName: String,
    val appName: String,
    val category: AppCategory,
    val isBlocked: Boolean
)
