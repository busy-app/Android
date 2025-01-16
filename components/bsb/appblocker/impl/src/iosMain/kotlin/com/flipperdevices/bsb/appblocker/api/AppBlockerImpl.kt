package com.flipperdevices.bsb.appblocker.api

import com.flipperdevices.bsb.preference.api.PreferenceApi
import com.flipperdevices.bsb.preference.api.get
import com.flipperdevices.bsb.preference.api.set
import com.flipperdevices.bsb.preference.model.SettingsEnum
import com.flipperdevices.core.di.AppGraph
import kotlinx.coroutines.runBlocking
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Inject
@ContributesBinding(AppGraph::class, AppBlockerApi::class)
class AppBlockerImpl(
    private val preferenceApi: PreferenceApi,
    private val familyControlApi: FamilyControlApi
) : AppBlockerApi {

    override fun isAppBlockerSupportActive(): Boolean {
        return preferenceApi.get(SettingsEnum.APP_BLOCKING, false)
    }

    override fun enableSupport(): Result<Unit> {
        if (familyControlApi.isAuthorized()) {
            preferenceApi.set(SettingsEnum.APP_BLOCKING, true)
            return Result.success(Unit)
        }

        return try {
            runBlocking { requestPermissions() }
            preferenceApi.set(SettingsEnum.APP_BLOCKING, true)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun disableSupport() {
        preferenceApi.set(SettingsEnum.APP_BLOCKING, false)
    }

    private suspend fun requestPermissions(): Unit = suspendCoroutine { continuation ->
        familyControlApi.familyControlsAuthorize(
            onAuthorized = {
                continuation.resume(Unit)
            },
            onDenied = {
                continuation.resumeWithException(RuntimeException("User denied permissions"))
            }
        )
    }
}
