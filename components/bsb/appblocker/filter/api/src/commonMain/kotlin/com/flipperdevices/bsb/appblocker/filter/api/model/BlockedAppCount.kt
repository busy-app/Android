package com.flipperdevices.bsb.appblocker.filter.api.model

import kotlinx.serialization.Serializable

@Serializable
sealed interface BlockedAppCount {
    @Serializable
    data object NoPermission : BlockedAppCount

    @Serializable
    data object TurnOff : BlockedAppCount

    @Serializable
    data object All : BlockedAppCount

    @Serializable
    data class Count(
        val count: Int
    ) : BlockedAppCount
}
