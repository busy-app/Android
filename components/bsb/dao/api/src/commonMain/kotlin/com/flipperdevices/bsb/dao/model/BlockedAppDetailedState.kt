package com.flipperdevices.bsb.dao.model

sealed interface BlockedAppDetailedState {
    data object All : BlockedAppDetailedState

    data object TurnOff : BlockedAppDetailedState

    data class TurnOnWhitelist(
        val entities: List<BlockedAppEntity>
    ) : BlockedAppDetailedState
}