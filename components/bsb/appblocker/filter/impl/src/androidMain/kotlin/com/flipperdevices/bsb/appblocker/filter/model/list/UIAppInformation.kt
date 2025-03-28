package com.flipperdevices.bsb.appblocker.filter.model.list

import com.flipperdevices.bsb.appblocker.filter.api.model.AppCategory

data class UIAppInformation(
    val packageName: String,
    val appName: String,
    val category: AppCategory,
    val isBlocked: Boolean
)
