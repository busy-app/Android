package com.flipperdevices.bsb.appblocker.filter.model.card

import kotlinx.collections.immutable.ImmutableList

sealed interface AppBlockerCardListState {
    data object Empty : AppBlockerCardListState

    data class Loaded(
        val isAllBlocked: Boolean,
        val icons: ImmutableList<AppIcon>
    ) : AppBlockerCardListState
}
