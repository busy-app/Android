package com.flipperdevices.bsb.wear.messenger.api

import kotlinx.coroutines.flow.StateFlow

interface WearConnectionApi {
    val statusFlow: StateFlow<Status>

    sealed interface Status {
        data object Disconnected : Status
        data object Connected : Status
    }
}
