package com.flipperdevices.bsb.timer.cards.viewmodel

import com.flipperdevices.bsb.analytics.metric.api.MetricApi
import com.flipperdevices.bsb.analytics.metric.api.model.BEvent
import com.flipperdevices.bsb.analytics.metric.api.model.TimerConfigSnapshot
import com.flipperdevices.bsb.dao.api.CardAppBlockerApi
import com.flipperdevices.bsb.dao.api.TimerSettingsApi
import com.flipperdevices.bsb.dao.model.BlockedAppCount
import com.flipperdevices.bsb.dao.model.BlockedAppDetailedState
import com.flipperdevices.bsb.dao.model.BlockedAppEntity
import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.timer.background.util.startWith
import com.flipperdevices.bsb.timer.cards.model.BusyCardModel
import com.flipperdevices.core.ui.lifecycle.DecomposeViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
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