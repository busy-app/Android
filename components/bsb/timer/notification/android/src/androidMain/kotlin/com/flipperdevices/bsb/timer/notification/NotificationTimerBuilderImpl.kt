package com.flipperdevices.bsb.timer.notification

import android.app.Notification
import android.content.Context
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState

class NotificationTimerBuilderImpl {
    fun buildStartUpNotification(
        context: Context
    ): Notification

    fun buildNotification(
        context: Context,
        timer: ControlledTimerState
    ): Notification?
}