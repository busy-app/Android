package com.flipperdevices.bsb.dao.api

import com.flipperdevices.bsb.appblocker.permission.api.AppBlockerPermissionApi
import com.flipperdevices.bsb.dao.model.AppDatabase
import com.flipperdevices.bsb.dao.model.BlockedAppCount
import com.flipperdevices.bsb.dao.model.BlockedAppDetailedState
import com.flipperdevices.bsb.dao.model.BlockedAppEntity
import com.flipperdevices.bsb.dao.model.TimerSettingsId
import com.flipperdevices.bsb.dao.model.cards.AppBlockerState
import com.flipperdevices.core.apppackage.AppDetailedInfoProvider
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.di.KIProvider
import com.flipperdevices.core.di.provideDelegate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
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

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getBlockedAppCount(cardId: TimerSettingsId): Flow<BlockedAppCount> {
        return permissionApi.isAllPermissionGranted().flatMapLatest { isPermissionGranted ->
            if (isPermissionGranted) {
                flowOf(BlockedAppCount.NoPermission)
            } else getBlockedAppDetailedState(cardId)
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

    override fun getBlockedAppDetailedState(cardId: TimerSettingsId): Flow<BlockedAppDetailedState> {
        return combine(
            database.cardRepository().getPlatformSpecificSettingFlow(cardId.id),
            database.blockedAppRepository().getBlockedCategories(cardId.id),
            database.blockedAppRepository().getBlockedApps(cardId.id),
        ) { cardSettings, blockedCategories, blockedApps ->
            return@combine when (cardSettings?.appBlockerState) {
                AppBlockerState.TURN_ON_ALL,
                null -> BlockedAppDetailedState.All

                AppBlockerState.TURN_OFF -> BlockedAppDetailedState.TurnOff
                AppBlockerState.TURN_ON_WHITE_LIST -> {
                    BlockedAppDetailedState.TurnOnWhitelist(
                        blockedCategories.map { BlockedAppEntity.Category(it.categoryId) } +
                                blockedApps.map { BlockedAppEntity.App(it.appPackage) }
                    )
                }
            }
        }
    }

    override suspend fun updateBlockedApp(
        cardId: TimerSettingsId,
        blockedAppState: BlockedAppDetailedState
    ) {
        val appBlockerState = when (blockedAppState) {
            BlockedAppDetailedState.All -> AppBlockerState.TURN_ON_ALL
            BlockedAppDetailedState.TurnOff -> AppBlockerState.TURN_OFF
            is BlockedAppDetailedState.TurnOnWhitelist -> AppBlockerState.TURN_ON_WHITE_LIST
        }
        database.cardRepository().updateAppBlockingState(
            cardId.id,
            appBlockerState
        )
        if (blockedAppState is BlockedAppDetailedState.TurnOnWhitelist) {
            database.blockedAppRepository().replace(
                cardId.id,
                blockedAppState.entities
            )
        }
    }

    override suspend fun isBlocked(cardId: TimerSettingsId, appEntity: BlockedAppEntity): Boolean {
        val platformSettings = database.cardRepository()
            .getPlatformSpecificSettingFlow(cardId.id)
            .first()

        return when (platformSettings?.appBlockerState) {
            AppBlockerState.TURN_ON_ALL,
            null -> true

            AppBlockerState.TURN_OFF -> false
            AppBlockerState.TURN_ON_WHITE_LIST -> isAppEntityBlocked(cardId, appEntity)
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