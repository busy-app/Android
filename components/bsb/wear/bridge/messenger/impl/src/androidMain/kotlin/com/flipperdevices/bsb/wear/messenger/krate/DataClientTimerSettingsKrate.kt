package com.flipperdevices.bsb.wear.messenger.krate

import com.flipperdevices.bsb.wear.messenger.model.WearOSTimerSettings
import com.flipperdevices.bsb.wear.messenger.model.WearOSTimerSettingsMessage
import com.flipperdevices.bsb.wear.messenger.util.dataItemsFlow
import com.flipperdevices.core.di.AppGraph
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataItem
import com.google.android.gms.wearable.PutDataRequest
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject
import ru.astrainteractive.klibs.kstorage.suspend.flow.FlowMutableKrate
import ru.astrainteractive.klibs.kstorage.suspend.impl.DefaultFlowMutableKrate
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(AppGraph::class, CloudWearOSTimerSettingsKrate::class)
class DataClientTimerSettingsKrate(
    dataClient: DataClient
) : CloudWearOSTimerSettingsKrate,
    FlowMutableKrate<ImmutableList<WearOSTimerSettings>> by DefaultFlowMutableKrate(
        factory = { persistentListOf() },
        loader = {
            dataClient.dataItemsFlow()
                .map { dataItem -> dataItem.filter { it.uri.path == WearOSTimerSettingsMessage.serializer.path } }
                .map { dataItem ->
                    dataItem
                        .mapNotNull(DataItem::getData)
                        .mapNotNull { runCatching { WearOSTimerSettingsMessage.serializer.decode(it) }.getOrNull() }
                        .lastOrNull()
                        .orEmpty()
                        .toPersistentList()
                }
        },
        saver = saver@{ value ->
            val byteArray = WearOSTimerSettingsMessage.serializer.encode(value)
            val request = PutDataRequest.create(WearOSTimerSettingsMessage.serializer.path).apply {
                data = byteArray
                setUrgent()
            }
            dataClient.putDataItem(request)
        }
    )
