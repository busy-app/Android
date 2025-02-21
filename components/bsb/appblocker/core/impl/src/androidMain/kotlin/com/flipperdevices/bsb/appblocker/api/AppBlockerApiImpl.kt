package com.flipperdevices.bsb.appblocker.api

import com.flipperdevices.bsb.appblocker.permission.api.AppBlockerPermissionApi
import com.flipperdevices.bsb.preference.api.PreferenceApi
import com.flipperdevices.bsb.preference.api.getFlow
import com.flipperdevices.bsb.preference.api.set
import com.flipperdevices.bsb.preference.model.SettingsEnum
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.log.LogTagProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(AppGraph::class, AppBlockerApi::class)
class AppBlockerApiImpl(
    private val preferenceApi: PreferenceApi,
    private val permissionApi: AppBlockerPermissionApi
) : AppBlockerApi, LogTagProvider {
    override val TAG = "AppBlockerApi"

    override fun isAppBlockerSupportActive(): Flow<Boolean> {
        return combine(
            permissionApi.isAllPermissionGranted(),
            preferenceApi.getFlow(SettingsEnum.APP_BLOCKING, true)
        ) { hasPermissions, appBlockingEnabled ->
            hasPermissions && appBlockingEnabled
        }
    }

    override fun enableSupport(): Result<Unit> {
        preferenceApi.set(SettingsEnum.APP_BLOCKING, true)
        return Result.success(Unit)
    }

    override fun disableSupport() {
        preferenceApi.set(SettingsEnum.APP_BLOCKING, false)
    }
}
