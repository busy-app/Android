package com.flipperdevices.bsb.appblocker.api

import platform.UIKit.UIViewController

interface FamilyControlApi {
    fun isAuthorized(): Boolean
    fun familyControlsAuthorize(
        onAuthorized: () -> Unit,
        onDenied: () -> Unit
    )

    fun enableFamilyControls()
    fun disableFamilyControls()

    fun count(): Int
    fun getVC(onDismiss: () -> Unit): UIViewController
}
