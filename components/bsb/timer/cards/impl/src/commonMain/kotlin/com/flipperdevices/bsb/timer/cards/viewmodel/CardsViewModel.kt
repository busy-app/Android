package com.flipperdevices.bsb.timer.cards.viewmodel

import com.flipperdevices.bsb.dao.api.CardAppBlockerApi
import com.flipperdevices.bsb.dao.api.TimerSettingsApi
import com.flipperdevices.bsb.timer.cards.model.BusyCardModel
import com.flipperdevices.core.ui.lifecycle.DecomposeViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject

@Inject
class CardsViewModel(
    private val timerSettingsApi: TimerSettingsApi,
    private val cardAppBlockerApi: CardAppBlockerApi
) : DecomposeViewModel() {
    private val state = timerSettingsApi.getTimerSettingsFlow().flatMapLatest { list ->
        combine(
            list.map { card ->
                cardAppBlockerApi.getBlockedAppCount(card.id)
                    .map { blockedAppCount ->
                        BusyCardModel(
                            settings = card,
                            blockedAppCount = blockedAppCount
                        )
                        blockedAppCount to card
                    }
            }
        ) { it.asList() }
    }

    fun getTimerSettingsState() = state
}