package com.flipperdevices.bsbwearable.card.viewmodel.data

import com.flipperdevices.bsb.wear.messenger.consumer.WearMessageConsumer
import com.flipperdevices.bsb.wear.messenger.consumer.bMessageFlow
import com.flipperdevices.bsb.wear.messenger.model.TimerSettingsMessage
import com.flipperdevices.bsb.wear.messenger.model.WearOSTimerSettings
import com.flipperdevices.core.di.AppGraph
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppGraph::class)
@ContributesBinding(AppGraph::class, CardStorageApi::class)
class CardStorageApiImpl(
    scope: CoroutineScope,
    wearMessageConsumer: WearMessageConsumer,
) : CardStorageApi {
    private val settingsMutableFlow = MutableStateFlow(
        persistentListOf<WearOSTimerSettings>()
    )

    override val settingFlow = settingsMutableFlow.asStateFlow()

    init {
        wearMessageConsumer.bMessageFlow
            .filterIsInstance<TimerSettingsMessage>()
            .onEach { timerSettingsMessage ->
                settingsMutableFlow.emit(timerSettingsMessage.instance.toPersistentList())
            }.launchIn(scope)
    }
}
