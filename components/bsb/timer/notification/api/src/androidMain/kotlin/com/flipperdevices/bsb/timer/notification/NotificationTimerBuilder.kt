package com.flipperdevices.bsb.timer.notification

import android.app.Notification
import android.content.Context
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState

interface NotificationTimerBuilder {
    fun buildStartUpNotification(
        context: Context
    ): Notification

    fun buildNotification(
        context: Context,
        timer: ControlledTimerState,
        timerPendingIntents: TimerPendingIntents
    ): Notification?
}