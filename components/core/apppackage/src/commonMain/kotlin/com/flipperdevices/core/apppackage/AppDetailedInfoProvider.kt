package com.flipperdevices.core.apppackage

interface AppDetailedInfoProvider {
    suspend fun provideAppInfo(packageId: String): AppDetailedInfo?
}
