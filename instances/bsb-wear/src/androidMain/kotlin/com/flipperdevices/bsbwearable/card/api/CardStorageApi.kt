package com.flipperdevices.bsbwearable.card.api

import android.util.Log
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnResume
import com.flipperdevices.bsb.appblocker.filter.api.model.BlockedAppCount
import com.flipperdevices.bsb.preference.model.TimerSettings
import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.wear.messenger.api.WearConnectionApi
import com.flipperdevices.bsb.wear.messenger.consumer.WearMessageConsumer
import com.flipperdevices.bsb.wear.messenger.consumer.bMessageFlow
import com.flipperdevices.bsb.wear.messenger.model.AppBlockerCountMessage
import com.flipperdevices.bsb.wear.messenger.model.AppBlockerCountRequestMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerSettingsMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerSettingsRequestMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerTimestampMessage
import com.flipperdevices.bsb.wear.messenger.model.TimerTimestampRequestMessage
import com.flipperdevices.bsb.wear.messenger.producer.WearMessageProducer
import com.flipperdevices.bsb.wear.messenger.producer.produce
import com.flipperdevices.core.di.AppGraph
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppGraph::class)
class CardStorageApi(
    private val scope: CoroutineScope,
    private val wearConnectionApi: WearConnectionApi,
    private val wearMessageConsumer: WearMessageConsumer,
    private val wearMessageProducer: WearMessageProducer,
    private val timerApi: TimerApi
) {

    private val settingsMutableFlow = MutableStateFlow<TimerSettings?>(null)
    private val appBlockerMutableFlow = MutableStateFlow<BlockedAppCount?>(null)

    val settingFlow = settingsMutableFlow.asStateFlow()
    val appBlockerFlow = appBlockerMutableFlow.asStateFlow()

    init {
        wearConnectionApi.statusFlow
            .filterIsInstance<WearConnectionApi.Status.Connected>()
            .onEach { connectionState ->
                wearMessageProducer.produce(TimerTimestampRequestMessage)
                wearMessageProducer.produce(TimerSettingsRequestMessage)
                wearMessageProducer.produce(AppBlockerCountRequestMessage)
            }.launchIn(scope)

        wearMessageConsumer
            .bMessageFlow
            .onEach { Log.d("RootDecomposeComponent", ": $it") }
            .filterIsInstance<TimerTimestampMessage>()
            .onEach { timerApi.setTimestampState(it.instance) }
            .launchIn(scope)

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