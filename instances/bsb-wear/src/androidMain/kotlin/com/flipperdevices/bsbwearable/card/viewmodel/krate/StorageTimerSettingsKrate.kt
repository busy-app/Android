package com.flipperdevices.bsbwearable.card.viewmodel.krate

import com.flipperdevices.bsb.wear.messenger.model.WearOSTimerSettings
import com.flipperdevices.bsb.wear.messenger.serializer.JsonWearMessageSerializer
import com.google.android.gms.wearable.DataClient
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import ru.astrainteractive.klibs.kstorage.suspend.flow.FlowMutableKrate
import ru.astrainteractive.klibs.kstorage.suspend.impl.DefaultFlowMutableKrate
import ru.astrainteractive.klibs.kstorage.suspend.value.FlowProvider

private const val KEY = "storage_timer_settings_cached"

class StorageTimerSettingsKrate(
    settings: ObservableSettings,
    json: Json = JsonWearMessageSerializer.DEFAULT_JSON
) : FlowMutableKrate<ImmutableList<WearOSTimerSettings>> by DefaultFlowMutableKrate(
    factory = { persistentListOf() },
    loader = FlowProvider {
        settings.toFlowSettings()
            .getStringOrNullFlow(KEY)
            .map { string ->
                if (string.isNullOrBlank()) return@map persistentListOf()
                runCatching {
                    json.decodeFromString<List<WearOSTimerSettings>>(string)
                        .toPersistentList()
                }.getOrNull() ?: persistentListOf()
            }
    },
    saver = { value ->
        val string = json.encodeToString(value.toList())
        settings.putString(KEY, string)
    }
)

