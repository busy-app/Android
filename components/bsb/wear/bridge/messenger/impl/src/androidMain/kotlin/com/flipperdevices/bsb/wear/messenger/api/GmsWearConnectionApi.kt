package com.flipperdevices.bsb.wear.messenger.api

import com.google.android.gms.wearable.Node
import kotlinx.coroutines.flow.StateFlow

interface GmsWearConnectionApi {
    val statusFlow: StateFlow<GmsStatus>

    sealed interface GmsStatus {
        data object Disconnected : GmsStatus
        data class Connected(val nodes: List<Node>) : GmsStatus
    }
}
