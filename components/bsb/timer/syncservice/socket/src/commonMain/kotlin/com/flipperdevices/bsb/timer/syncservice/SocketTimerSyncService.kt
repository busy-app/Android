package com.flipperdevices.bsb.timer.syncservice

import com.flipperdevices.bsb.cloud.mock.api.BSBMockApi
import com.flipperdevices.bsb.preference.api.PreferenceApi
import com.flipperdevices.bsb.preference.model.SettingsEnum
import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.timer.background.model.TimerTimestamp
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.log.error
import com.flipperdevices.core.log.info
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.json
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppGraph::class)
@ContributesBinding(AppGraph::class, TimerSyncService::class)
class SocketTimerSyncService(
    private val scope: CoroutineScope,
    private val preferenceApi: PreferenceApi,
    private val timerApi: TimerApi,
    private val bsbMockApi: BSBMockApi
) : TimerSyncService, LogTagProvider {
    override val TAG: String = "SocketTimerSyncService"

    private val json = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
        coerceInputValues = true
    }
    private val client = HttpClient(CIO) {
        install(WebSockets) {
            @Suppress("MagicNumber")
            pingIntervalMillis = 20_000
        }
        install(ContentNegotiation) {
            json(json)
        }
    }
    private val socketFlow = callbackFlow {
        val sessionFlow = MutableSharedFlow<DefaultClientWebSocketSession>(1)
        do {
            kotlin.runCatching {
                sessionFlow.emit(
                    client.webSocketSession(
                        method = HttpMethod.Get,
                        host = "192.168.0.108",
                        port = 8080,
                        path = "/api/v0/timersync",
                        block = {
                            header(
                                key = HttpHeaders.Authorization,
                                value = preferenceApi.getString(
                                    SettingsEnum.AUTH_TOKEN,
                                    null
                                )
                            )
                        }
                    )
                )
            }.onFailure { it.printStackTrace() }
            sessionFlow.firstOrNull()?.let { session ->
                send(session)
                session.closeReason.await()
            }
            error { "#socketFlow Reconnecting..." }
            delay(SOCKET_RECONNECT_DELAY_MILLIS)
        } while (currentCoroutineContext().isActive)
        awaitClose {
            info { "#socketFlow Session ended!" }
            runBlocking { sessionFlow.firstOrNull()?.close() }
        }
    }.shareIn(scope, SharingStarted.Eagerly, 1)

    private fun listenForTimerChange() {
        timerApi.getTimestampState()
            .distinctUntilChanged { old, new -> old == new }
            .filter { bsbMockApi.getTimer().getOrNull() != it }
            .onEach { bsbMockApi.saveTimer(it) }
            .launchIn(scope)
    }

    private fun listenForSocketEvent() {
        socketFlow
            .flatMapLatest { it.incoming.receiveAsFlow() }
            .onEach {
                when (it) {
                    is Frame.Binary -> info { "#onCreate server -> Binary" }
                    is Frame.Close -> info { "#onCreate server -> Close" }
                    is Frame.Ping -> info { "#onCreate server -> Ping" }
                    is Frame.Pong -> info { "#onCreate server -> Pong" }
                    is Frame.Text -> {
                        val text = it.readText()
                        kotlin.runCatching {
                            json.decodeFromString<TimerTimestamp>(text)
                        }.onSuccess {
                            timerApi.setTimestampState(it)
                        }
                        info { "#onCreate server -> Text -> ${it.readText()}" }
                    }

                    else -> info { "#onCreate server -> ELSE" }
                }
            }
            .launchIn(scope)
    }

    override fun onCreate() {
        listenForTimerChange()
        listenForSocketEvent()
    }
    companion object {
        private const val SOCKET_RECONNECT_DELAY_MILLIS = 5000L
    }
}
