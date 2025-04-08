package com.flipperdevices.bsb.wear.messenger.util

import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataItem
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

fun DataClient.dataItemsFlow(): Flow<List<DataItem>> = callbackFlow {
    send(dataItems.await().toList())
    val listener = DataClient.OnDataChangedListener { dataEventBuffer ->
        if (dataEventBuffer.isClosed) return@OnDataChangedListener
        launch {
            send(dataEventBuffer.map(DataEvent::getDataItem))
        }
    }
    addListener(listener)

    awaitClose {
        removeListener(listener)
    }
}

