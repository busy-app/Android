package com.flipperdevices.bsbwearable.card.viewmodel.data

import com.flipperdevices.bsb.wear.messenger.consumer.WearMessageConsumer
import com.flipperdevices.bsb.wear.messenger.consumer.bMessageFlow
import com.flipperdevices.bsb.wear.messenger.krate.CloudWearOSTimerSettingsKrate
import com.flipperdevices.bsb.wear.messenger.model.TimerSettingsMessage
import com.flipperdevices.bsbwearable.card.viewmodel.krate.ComposedTimerSettingsKrate
import com.flipperdevices.bsbwearable.card.viewmodel.krate.StorageTimerSettingsKrate
import com.flipperdevices.core.di.AppGraph
import com.russhwolf.settings.ObservableSettings
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.tatarka.inject.annotations.Inject
import ru.astrainteractive.klibs.kstorage.util.KrateExt.update
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppGraph::class)
@ContributesBinding(AppGraph::class, CardStorageApi::class)
class CardStorageApiImpl(
    scope: CoroutineScope,
    wearMessageConsumer: WearMessageConsumer,
    settings: ObservableSettings,
    cloudWearOSTimerSettingsKrate: CloudWearOSTimerSettingsKrate
) : CardStorageApi {
    private val timerSettingsKrate = ComposedTimerSettingsKrate(
        dataClientTimerSettingsKrate = cloudWearOSTimerSettingsKrate,
        storageTimerSettingsKrate = StorageTimerSettingsKrate(settings)
    )

    override val settingFlow = timerSettingsKrate.stateFlow(scope)

    init {
        wearMessageConsumer.bMessageFlow
            .filterIsInstance<TimerSettingsMessage>()
            .onEach { timerSettingsMessage ->
                timerSettingsKrate.update {
                    timerSettingsMessage.instance.toImmutableList()
                }
            }.launchIn(scope)
    }
}
