package com.flipperdevices.bsb.appblocker.api

interface FamilyControlApi {
    fun isAuthorized(): Boolean
    fun familyControlsAuthorize(
        onAuthorized: () -> Unit,
        onDenied: () -> Unit
    )

    fun enableFamilyControls()
    fun disableFamilyControls()
}