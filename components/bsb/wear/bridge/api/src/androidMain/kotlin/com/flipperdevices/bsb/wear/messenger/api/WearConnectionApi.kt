package com.flipperdevices.bsb.wear.messenger.api

import com.google.android.gms.wearable.Node
import kotlinx.coroutines.flow.StateFlow

interface WearConnectionApi {
    val statusFlow: StateFlow<Status>

    sealed interface Status {
        data object Disconnected : Status
        data class Connected(val node: Node) : Status

        val nodeOrNull: Node?
            get() = (this as? Connected)?.node
    }
}
