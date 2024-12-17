package com.flipperdevices.bsb.preferencescreen.model

import com.flipperdevices.bsb.cloud.model.BSBUser

data class PreferenceScreenState(
    val isDndActive: Boolean,
    val isAppBlockActive: Boolean,
    val devMode: Boolean,
    val bsbUser: BSBUser? = null
)
