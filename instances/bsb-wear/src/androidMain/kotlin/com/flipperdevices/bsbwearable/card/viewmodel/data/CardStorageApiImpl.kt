package com.flipperdevices.bsbwearable.card.viewmodel.data

import com.flipperdevices.bsb.appblocker.filter.api.model.BlockedAppCount
import com.flipperdevices.bsb.preference.model.TimerSettings
import com.flipperdevices.bsb.wear.messenger.consumer.WearMessageConsumer
import com.flipperdevices.bsb.wear.messenger.consumer.bMessageFlow
import com.flipperdevices.bsb.wear.messenger.model.AppBlockerCountMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerSettingsMessage
import com.flipperdevices.core.di.AppGraph
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
        TimerSettings(
            intervalsSettings = TimerSettings.IntervalsSettings(
                isEnabled = true
            )
        )
    )
    private val appBlockerMutableFlow = MutableStateFlow<BlockedAppCount?>(null)

    override val settingFlow: StateFlow<TimerSettings> = settingsMutableFlow.asStateFlow()
    override val appBlockerFlow: StateFlow<BlockedAppCount?> = appBlockerMutableFlow.asStateFlow()

    init {
        wearMessageConsumer.bMessageFlow
            .filterIsInstance<AppBlockerCountMessage>()
            .onEach { appBlockerCountMessage ->
                appBlockerMutableFlow.emit(appBlockerCountMessage.instance)
            }.launchIn(scope)

        wearMessageConsumer.bMessageFlow
            .filterIsInstance<TimerSettingsMessage>()
            .onEach { timerSettingsMessage ->
                settingsMutableFlow.emit(timerSettingsMessage.instance)
            }.launchIn(scope)
    }
}
