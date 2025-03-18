package com.flipperdevices.bsb.wear.messenger.util

import com.flipperdevices.core.log.TaggedLogger
import com.flipperdevices.core.log.error
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.Node
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

const val CAPABILITY_PHONE_APP = "verify_remote_flipper_phone_app"

private val logger = TaggedLogger("CapabilityExt")

val CapabilityClient.nodesFlow: Flow<List<Node>>
    get() = callbackFlow {
        val listener = CapabilityClient.OnCapabilityChangedListener { capabilityInfo ->
            launch {
                val nodes = capabilityInfo.nodes.filterNotNull()
                send(nodes)
            }
        }

        try {
            addListener(listener, CAPABILITY_PHONE_APP).await()
            val capabilityInfo = getCapability(
                CAPABILITY_PHONE_APP,
                CapabilityClient.FILTER_ALL
            ).await()
            send(capabilityInfo.nodes.filterNotNull())
        } catch (e: ApiException) {
            logger.error(e) { "#nodesFlow could not find wearable api while enabling" }
        } catch (e: Throwable) {
            logger.error(e) { "#nodesFlow unhandled exception during enable" }
        } finally {
            close()
        }

        awaitClose {
            runBlocking {
                try {
                    removeListener(listener, CAPABILITY_PHONE_APP).await()
                } catch (e: ApiException) {
                    logger.error(e) { "#nodesFlow could not find wearable api while disabling" }
                } catch (e: Throwable) {
                    logger.error(e) { "#nodesFlow unhandled exception during disable" }
                }
            }
        }
    }
