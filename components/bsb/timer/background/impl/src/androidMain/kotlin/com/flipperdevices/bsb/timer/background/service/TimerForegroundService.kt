package com.flipperdevices.bsb.timer.background.service

import android.app.NotificationManager
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.timer.background.api.TimerStateListener
import com.flipperdevices.bsb.timer.background.di.ServiceDIComponent
import com.flipperdevices.bsb.timer.background.model.TimerTimestamp
import com.flipperdevices.bsb.timer.background.util.pause
import com.flipperdevices.bsb.timer.background.util.resume
import com.flipperdevices.core.di.ComponentHolder
import com.flipperdevices.core.ktx.android.toFullString
import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.log.error
import com.flipperdevices.core.log.info
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.plus
import kotlinx.serialization.json.Json

internal const val EXTRA_KEY_TIMER_STATE = "timer_state"

private const val NOTIFICATION_ID = 100

class TimerForegroundService : LifecycleService(), LogTagProvider, TimerStateListener {
    override val TAG = "TimerForegroundService"

    private val serviceDIComponent = ComponentHolder.component<ServiceDIComponent>()
    private val delegate = serviceDIComponent.commonTimerApi
    private val notificationBuilder = serviceDIComponent.notificationBuilder

    private val binder = TimerServiceBinder(delegate)
    private val notificationManager by lazy { getSystemService(NotificationManager::class.java) }

    init {
        delegate.getState()
            .onEach { state ->
                val notification = notificationBuilder.buildNotification(
                    this@TimerForegroundService,
                    state
                )
                if (notification == null) {
                    notificationManager.cancel(NOTIFICATION_ID)
                } else {
                    notificationManager.notify(NOTIFICATION_ID, notification)
                }
            }.launchIn(lifecycleScope + Dispatchers.Main)
    }

    override fun onCreate() {
        info { "onCreate" }
        super.onCreate()

        delegate.addListener(this)

        startForeground(
            NOTIFICATION_ID,
            notificationBuilder.buildStartUpNotification(applicationContext)
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        info { "Receive command ${intent?.toFullString()}" }
        super.onStartCommand(intent, flags, startId)

        val serviceAction = intent?.action
        if (serviceAction != null) {
            when (serviceAction) {
                TimerServiceActionEnum.START.actionId -> {
                    val timerStateString = intent.getStringExtra(EXTRA_KEY_TIMER_STATE)
                    if (timerStateString != null) {
                        val timerState = Json.decodeFromString<TimerTimestamp>(timerStateString)
                        delegate.setTimestampState(timerState)
                    } else {
                        error { "Not found timer start" }
                        delegate.setTimestampState(null)
                    }
                }

                TimerServiceActionEnum.STOP.actionId -> {
                    delegate.setTimestampState(null)
                    stopServiceInternal()
                }

                TimerServiceActionEnum.RESUME.actionId -> {
                    delegate.resume()
                }

                TimerServiceActionEnum.PAUSE.actionId -> {
                    delegate.pause()
                }
            }
        } else {
            error { "Receive intent without action" }
        }

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        info { "On bind ${intent.toFullString()}" }
        super.onBind(intent)
        return binder
    }

    private fun stopServiceInternal() {
        stopForeground(STOP_FOREGROUND_REMOVE)
        notificationManager.cancel(NOTIFICATION_ID)
        stopSelf()
    }

    override fun onTimerStop() {
        stopServiceInternal()
    }

    override fun onDestroy() {
        super.onDestroy()
        delegate.removeListener(this)
    }
}

class TimerServiceBinder(
    val timerApi: TimerApi
) : Binder()
