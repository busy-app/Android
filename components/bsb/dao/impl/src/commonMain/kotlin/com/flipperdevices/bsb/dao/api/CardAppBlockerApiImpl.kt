package com.flipperdevices.bsb.dao.api

import com.flipperdevices.bsb.appblocker.permission.api.AppBlockerPermissionApi
import com.flipperdevices.bsb.dao.model.AppDatabase
import com.flipperdevices.bsb.dao.model.BlockedAppCount
import com.flipperdevices.bsb.dao.model.BlockedAppDetailedState
import com.flipperdevices.bsb.dao.model.TimerSettingsId
import com.flipperdevices.bsb.dao.model.cards.AppBlockerState
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.di.KIProvider
import com.flipperdevices.core.di.provideDelegate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(AppGraph::class, CardAppBlockerApi::class)
class CardAppBlockerApiImpl(
    private val permissionApi: AppBlockerPermissionApi,
    databaseProvider: KIProvider<AppDatabase>
) : CardAppBlockerApi {
    private val database by databaseProvider

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getBlockedAppCount(cardId: TimerSettingsId): Flow<BlockedAppCount> {
        return permissionApi.isAllPermissionGranted().flatMapLatest { isPermissionGranted ->
            return@flatMapLatest if (!isPermissionGranted) {
                flowOf(BlockedAppCount.NoPermission)
            } else combine(
                database.cardRepository().getPlatformSpecificSetting(cardId.id),
                database.blockedAppRepository().getBlockedCategories(cardId.id),
                database.blockedAppRepository().getBlockedApp(cardId.id),
            ) { cardSettings, blockedCategories, blockedApps ->
                return@combine when (cardSettings?.appBlockerState) {
                    AppBlockerState.TURN_ON_ALL,
                    null -> BlockedAppCount.All

                    AppBlockerState.TURN_OFF -> BlockedAppCount.TurnOff
                    AppBlockerState.TURN_ON_WHITE_LIST -> {
                        BlockedAppCount.Count(blockedCategories.count() + blockedApps.count())
                    }
                }
            }
        }
    }

    override suspend fun getBlockedAppDetailedState(cardId: TimerSettingsId): BlockedAppDetailedState {
        TODO("Not yet implemented")
    }

    override suspend fun updateBlockedApp(cardId: TimerSettingsId, blockedAppState: BlockedAppDetailedState) {
        TODO("Not yet implemented")
    }
}