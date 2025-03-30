package com.flipperdevices.bsb.appblocker.filter.viewmodel.card

import com.flipperdevices.bsb.appblocker.filter.api.model.AppCategory
import com.flipperdevices.bsb.appblocker.filter.model.card.AppBlockerCardListState
import com.flipperdevices.bsb.appblocker.filter.model.card.AppIcon
import com.flipperdevices.bsb.appblocker.filter.model.list.fromCategoryId
import com.flipperdevices.bsb.appblocker.filter.model.list.icon
import com.flipperdevices.bsb.dao.api.CardAppBlockerApi
import com.flipperdevices.bsb.dao.model.BlockedAppDetailedState
import com.flipperdevices.bsb.dao.model.BlockedAppEntity
import com.flipperdevices.bsb.dao.model.TimerSettingsId
import com.flipperdevices.core.ui.lifecycle.DecomposeViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class AppBlockerCardListViewModel(
    @Assisted timerSettingsId: TimerSettingsId,
    cardAppBlockerApi: CardAppBlockerApi
) : DecomposeViewModel() {
    private val listState = MutableStateFlow<AppBlockerCardListState>(AppBlockerCardListState.Empty)

    init {
        cardAppBlockerApi.getBlockedAppDetailedState(timerSettingsId)
            .map { blockedAppDetailedState ->
                when (blockedAppDetailedState) {
                    BlockedAppDetailedState.All -> AppBlockerCardListState.Loaded(
                        isAllBlocked = true,
                        icons = AppCategory.entries
                            .map { AppIcon.Category(it.icon) }
                            .toPersistentList()
                    )

                    BlockedAppDetailedState.TurnOff -> AppBlockerCardListState.Empty
                    is BlockedAppDetailedState.TurnOnWhitelist -> AppBlockerCardListState.Loaded(
                        isAllBlocked = false,
                        icons = blockedAppDetailedState.entities.sorted().map {
                            when (it) {
                                is BlockedAppEntity.App -> AppIcon.App(it.packageId)
                                is BlockedAppEntity.Category -> AppIcon.Category(
                                    icon = AppCategory.fromCategoryId(it.categoryId).icon
                                )
                            }
                        }.toPersistentList()
                    )
                }
            }.onEach {
                listState.emit(it)
            }.launchIn(viewModelScope)
    }

    fun getState() = listState.asStateFlow()
}
