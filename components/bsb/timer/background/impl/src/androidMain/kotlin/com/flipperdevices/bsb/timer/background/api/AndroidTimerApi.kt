package com.flipperdevices.bsb.timer.background.api

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.background.service.EXTRA_KEY_TIMER_STATE
import com.flipperdevices.bsb.timer.background.service.TimerForegroundService
import com.flipperdevices.bsb.timer.background.service.TimerServiceActionEnum
import com.flipperdevices.bsb.timer.background.service.TimerServiceBinder
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.ktx.common.withLock
import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.log.error
import com.flipperdevices.core.log.info
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppGraph::class)
@ContributesBinding(AppGraph::class, TimerApi::class)
class AndroidTimerApi(
    private val scope: CoroutineScope,
    private val context: Context
) : TimerApi, ServiceConnection, LogTagProvider {
    override val TAG = "AndroidTimerApi"

    private val timerStateFlow = MutableStateFlow<ControlledTimerState>(ControlledTimerState.NotStarted)
    private val timerTimestampFlow = MutableStateFlow<TimerTimestamp?>(null)

    private val mutex = Mutex()
    private var binderListenerJob: Job? = null

    override fun setTimestampState(state: TimerTimestamp?) {
        info { "Request start timer via android service timer api" }
        if (state == null) {
            stopTimer()
            return
        }
        val intent = Intent(context, TimerForegroundService::class.java)
        intent.setAction(TimerServiceActionEnum.START.actionId)
        runBlocking { timerTimestampFlow.emit(state) }
        intent.putExtra(EXTRA_KEY_TIMER_STATE, Json.encodeToString(state))
        context.startService(intent)
        runBlocking {
            withLock(mutex) {
                if (binderListenerJob == null) {
                    val bindSuccessful = context.bindService(
                        Intent(context, TimerForegroundService::class.java),
                        this@AndroidTimerApi,
                        Context.BIND_AUTO_CREATE or Context.BIND_IMPORTANT
                    )
                    info { "Bind for service is $bindSuccessful" }
                }
            }
        }
    }

    private fun stopTimer() {
        timerTimestampFlow.value = null
        val intent = Intent(context, TimerForegroundService::class.java)
        intent.setAction(TimerServiceActionEnum.STOP.actionId)
        context.startService(intent)
    }

    override fun getTimestampState(): StateFlow<TimerTimestamp?> {
        return timerTimestampFlow.asStateFlow()
    }

    override fun getState() = timerStateFlow.asStateFlow()

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val timerBinder = service as? TimerServiceBinder
        if (timerBinder == null) {
            error { "Try to connect with $name failed because binder is not TimerServiceBinder" }
            return
        }
        runBlocking {
            withLock(mutex) {
                binderListenerJob = getJob(timerBinder)
            }
        }
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        runBlocking {
            withLock(mutex) {
                binderListenerJob?.cancelAndJoin()
                binderListenerJob = null
                timerStateFlow.emit(ControlledTimerState.Finished)
            }
        }
    }

    private fun getJob(binder: TimerServiceBinder) = binder
        .timerApi
        .getState()
        .onEach { state -> timerStateFlow.emit(state) }
        .launchIn(scope)
}
