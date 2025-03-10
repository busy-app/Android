package com.flipperdevices.bsb.wear.messenger.util

import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.Node
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

const val CAPABILITY_PHONE_APP = "verify_remote_flipper_phone_app"

val CapabilityClient.nodesFlow: Flow<List<Node>>
    get() = callbackFlow {
        val listener = CapabilityClient.OnCapabilityChangedListener { capabilityInfo ->
            launch {
                val nodes = capabilityInfo.nodes.filterNotNull()
                send(nodes)
            }
        }

        addListener(listener, CAPABILITY_PHONE_APP).await()

        val capabilityInfo = getCapability(
            CAPABILITY_PHONE_APP,
            CapabilityClient.FILTER_ALL
        ).await()
        send(capabilityInfo.nodes.filterNotNull())

        awaitClose {
            runBlocking {
                removeListener(listener, CAPABILITY_PHONE_APP).await()
            }
        }
    }
