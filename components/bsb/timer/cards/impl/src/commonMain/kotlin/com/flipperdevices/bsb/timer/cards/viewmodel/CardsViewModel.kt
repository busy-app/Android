package com.flipperdevices.bsb.timer.cards.viewmodel

import com.flipperdevices.bsb.dao.api.CardAppBlockerApi
import com.flipperdevices.bsb.dao.api.TimerSettingsApi
import com.flipperdevices.bsb.timer.cards.model.BusyCardModel
import com.flipperdevices.core.ui.lifecycle.DecomposeViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import me.tatarka.inject.annotations.Inject

@Inject
class CardsViewModel(
    timerSettingsApi: TimerSettingsApi,
    private val cardAppBlockerApi: CardAppBlockerApi,
) : DecomposeViewModel() {
    @OptIn(ExperimentalCoroutinesApi::class)
    private val state = timerSettingsApi.getTimerSettingsListFlow().flatMapLatest { list ->
        combine(
            list.map { card ->
                cardAppBlockerApi.getBlockedAppCount(card.id)
                    .map { blockedAppCount ->
                        BusyCardModel(
                            settings = card,
                            blockedAppCount = blockedAppCount
                        )
                    }
            }
        ) { it.toImmutableList() }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), persistentListOf())

    fun getTimerSettingsState(): StateFlow<ImmutableList<BusyCardModel>> = state
}
