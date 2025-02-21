package com.flipperdevices.bsb.appblocker.filter.api.model

sealed interface BlockedAppCount {
    data object NoPermission : BlockedAppCount
    data object TurnOff : BlockedAppCount
    data object All : BlockedAppCount
    data class Count(
        val count: Int
    ) : BlockedAppCount
}
