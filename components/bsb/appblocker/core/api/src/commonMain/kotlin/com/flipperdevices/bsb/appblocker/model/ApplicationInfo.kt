package com.flipperdevices.bsb.appblocker.model

import kotlinx.serialization.Serializable

@Serializable
data class ApplicationInfo(
    val packageName: String,
    val openCount: Int
)
