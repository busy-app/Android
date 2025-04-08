package com.flipperdevices.bsbwearable.card.viewmodel.data

import com.flipperdevices.bsb.wear.messenger.krate.CloudWearOSTimerSettingsKrate
import com.flipperdevices.bsbwearable.card.viewmodel.krate.ComposedTimerSettingsKrate
import com.flipperdevices.bsbwearable.card.viewmodel.krate.StorageTimerSettingsKrate
import com.flipperdevices.core.di.AppGraph
import com.russhwolf.settings.ObservableSettings
import kotlinx.coroutines.CoroutineScope
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppGraph::class)
@ContributesBinding(AppGraph::class, CardStorageApi::class)
class CardStorageApiImpl(
    scope: CoroutineScope,
    settings: ObservableSettings,
    cloudWearOSTimerSettingsKrate: CloudWearOSTimerSettingsKrate
) : CardStorageApi {
    private val timerSettingsKrate = ComposedTimerSettingsKrate(
        dataClientTimerSettingsKrate = cloudWearOSTimerSettingsKrate,
        storageTimerSettingsKrate = StorageTimerSettingsKrate(settings)
    )

    override val settingFlow = timerSettingsKrate.stateFlow(scope)
}
