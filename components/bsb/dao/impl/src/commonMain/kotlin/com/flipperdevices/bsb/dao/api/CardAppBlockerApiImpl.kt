package com.flipperdevices.bsb.dao.api

import com.flipperdevices.bsb.appblocker.permission.api.AppBlockerPermissionApi
import com.flipperdevices.bsb.dao.model.AppDatabase
import com.flipperdevices.bsb.dao.model.BlockedAppCount
import com.flipperdevices.bsb.dao.model.BlockedAppDetailedState
import com.flipperdevices.bsb.dao.model.BlockedAppEntity
import com.flipperdevices.bsb.dao.model.TimerSettingsId
import com.flipperdevices.core.apppackage.AppDetailedInfoProvider
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.di.KIProvider
import com.flipperdevices.core.di.provideDelegate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(AppGraph::class, CardAppBlockerApi::class)
class CardAppBlockerApiImpl(
    private val permissionApi: AppBlockerPermissionApi,
    databaseProvider: KIProvider<AppDatabase>,
    private val appInfoProvider: AppDetailedInfoProvider
) : CardAppBlockerApi {
    private val database by databaseProvider

    private val cardUpdateSharedFlow = MutableSharedFlow<TimerSettingsId>()

    override fun getCardUpdateFlow(): Flow<TimerSettingsId> {
        return cardUpdateSharedFlow.asSharedFlow()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getBlockedAppCount(cardId: TimerSettingsId): Flow<BlockedAppCount> {
        return permissionApi.isAllPermissionGranted().flatMapLatest { isPermissionGranted ->
            if (!isPermissionGranted) {
                flowOf(BlockedAppCount.NoPermission)
            } else {
                getBlockedAppDetailedState(cardId)
                    .map {
                        when (it) {
                            BlockedAppDetailedState.All -> BlockedAppCount.All
                            BlockedAppDetailedState.TurnOff -> BlockedAppCount.TurnOff
                            is BlockedAppDetailedState.TurnOnWhitelist -> BlockedAppCount.Count(
                                it.entities.size
                            )
                        }
                    }
            }
        }
    }

    override fun getBlockedAppDetailedState(cardId: TimerSettingsId): Flow<BlockedAppDetailedState> {
        return combine(
            database.cardRepository().getPlatformSpecificSettingFlow(cardId.id),
            database.blockedAppRepository().getBlockedCategories(cardId.id),
            database.blockedAppRepository().getBlockedApps(cardId.id),
        ) { cardSettings, blockedCategories, blockedApps ->
            return@combine if (cardSettings == null) {
                BlockedAppDetailedState.All
            } else if (cardSettings.isBlockedEnabled.not()) {
                BlockedAppDetailedState.TurnOff
            } else if (cardSettings.isBlockedAll) {
                BlockedAppDetailedState.All
            } else {
                BlockedAppDetailedState.TurnOnWhitelist(
                    blockedCategories.map { BlockedAppEntity.Category(it.categoryId) } +
                            blockedApps.map { BlockedAppEntity.App(it.appPackage) }
                )
            }
        }
    }

    override fun isEnabled(cardId: TimerSettingsId): Flow<Boolean> {
        return combine(
            permissionApi.isAllPermissionGranted(),
            database.cardRepository().getPlatformSpecificSettingFlow(cardId.id)
        ) { isPermissionEnabled, platformSettings ->
            isPermissionEnabled && platformSettings?.isBlockedEnabled ?: true
        }
    }

    override suspend fun setEnabled(cardId: TimerSettingsId, isEnabled: Boolean) {
        database.cardRepository().updateBlockedEnabled(cardId.id, isEnabled)
        cardUpdateSharedFlow.emit(cardId)
    }

    override suspend fun updateBlockedApp(
        cardId: TimerSettingsId,
        blockedAppState: BlockedAppDetailedState
    ) {
        val repository = database.cardRepository()
        when (blockedAppState) {
            BlockedAppDetailedState.All -> {
                repository.updateBlockedEnabled(cardId.id, true)
                repository.updateBlockedAll(cardId.id, true)
            }

            is BlockedAppDetailedState.TurnOnWhitelist -> {
                repository.updateBlockedEnabled(cardId.id, true)
                repository.updateBlockedAll(cardId.id, false)
            }

            BlockedAppDetailedState.TurnOff -> {
                repository.updateBlockedEnabled(cardId.id, false)
            }
        }

        if (blockedAppState is BlockedAppDetailedState.TurnOnWhitelist) {
            database.blockedAppRepository().replace(
                cardId.id,
                blockedAppState.entities
            )
        }
        cardUpdateSharedFlow.emit(cardId)
    }

    override suspend fun isBlocked(cardId: TimerSettingsId, appEntity: BlockedAppEntity): Boolean {
        val platformSettings = database.cardRepository()
            .getPlatformSpecificSettingFlow(cardId.id)
            .first()

        return if (platformSettings == null) {
            isAppEntityBlocked(cardId, appEntity)
        } else if (platformSettings.isBlockedEnabled.not()) {
            false
        } else if (platformSettings.isBlockedAll) {
            true
        } else {
            isAppEntityBlocked(cardId, appEntity)
        }
    }

    private suspend fun isAppEntityBlocked(
        cardId: TimerSettingsId,
        appEntity: BlockedAppEntity
    ): Boolean {
        val categories = database.blockedAppRepository().getBlockedCategories(cardId.id)
            .first()
            .map { it.categoryId }

        val appPackage = when (appEntity) {
            is BlockedAppEntity.Category -> return categories.contains(appEntity.categoryId)
            is BlockedAppEntity.App -> appEntity.packageId // Continue flow
        }

        val blockedApp = database.blockedAppRepository().getBlockedApp(cardId.id, appPackage)

        if (blockedApp != null) {
            return true
        }

        val appInfo = appInfoProvider.provideAppInfo(appPackage) ?: return false

        return categories.contains(appInfo.categoryId)
    }
}
