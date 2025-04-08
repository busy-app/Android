package com.flipperdevices.bsbwearable.card.viewmodel.krate

import com.flipperdevices.bsb.wear.messenger.krate.CloudWearOSTimerSettingsKrate
import com.flipperdevices.bsb.wear.messenger.model.WearOSTimerSettings
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.astrainteractive.klibs.kstorage.suspend.flow.FlowMutableKrate
import ru.astrainteractive.klibs.kstorage.suspend.impl.DefaultFlowMutableKrate

class ComposedTimerSettingsKrate(
    dataClientTimerSettingsKrate: CloudWearOSTimerSettingsKrate,
    storageTimerSettingsKrate: StorageTimerSettingsKrate
) : FlowMutableKrate<ImmutableList<WearOSTimerSettings>> by DefaultFlowMutableKrate(
    factory = { persistentListOf() },
    loader = {
        channelFlow {
            // We don't want to load old local storage when already have newer cloud settings
            val hasDataClientEmit = MutableStateFlow(false)
            dataClientTimerSettingsKrate.flow
                .onEach {
                    hasDataClientEmit.emit(true)
                    send(it)
                }.launchIn(this)

            storageTimerSettingsKrate.flow
                .filter { !hasDataClientEmit.first() }
                .onEach { send(it) }.launchIn(this)
        }
    },
    saver = saver@{ value ->
        dataClientTimerSettingsKrate.save(value)
        storageTimerSettingsKrate.save(value)
    }
)
