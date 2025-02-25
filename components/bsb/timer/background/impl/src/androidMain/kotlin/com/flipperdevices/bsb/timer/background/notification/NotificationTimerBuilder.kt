package com.flipperdevices.bsb.timer.background.notification

import android.app.Notification
import android.content.Context
import android.widget.RemoteViews
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.flipperdevices.bsb.timer.background.impl.R
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.background.model.toHumanReadableString

private const val TIMER_NOTIFICATION_CHANNEL = "timer_notification_channel"

object NotificationTimerBuilder {
    fun buildStartUpNotification(
        context: Context
    ): Notification {
        createChannelIfNotYet(context)

        return NotificationCompat.Builder(context, TIMER_NOTIFICATION_CHANNEL)
            .setSilent(true)
            .setSmallIcon(R.drawable.ic_busy_logo)
            .setColor(context.getColor(R.color.brand_color))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentTitle(context.getString(R.string.timer_notification_title))
            .setContentText(context.getString(R.string.timer_notification_desc_empty))
            .build()
    }

    fun buildNotification(
        context: Context,
        timer: ControlledTimerState = ControlledTimerState.NotStarted
    ): Notification? {
        createChannelIfNotYet(context)
        if (timer !is ControlledTimerState.Running) {
            return null
        }

        val notificationBuilder = NotificationCompat.Builder(context, TIMER_NOTIFICATION_CHANNEL)
            .setSilent(true)
            .setSmallIcon(R.drawable.ic_busy_logo)
            .setColor(context.getColor(R.color.brand_color))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationLayout = RemoteViews(
            context.packageName,
            R.layout.notification_base_layout
        )
        notificationLayout.setTextViewText(
            R.id.timer,
            timer.toHumanReadableString()
        )
        notificationLayout.setImageViewResource(
            R.id.status_pic,
            timer.getStatusImageId()
        )
        notificationBuilder.setCustomContentView(notificationLayout)

        return notificationBuilder.build()
    }

    private fun createChannelIfNotYet(context: Context) {
        val notificationManager = NotificationManagerCompat.from(context)

        val flipperChannel = NotificationChannelCompat.Builder(
            TIMER_NOTIFICATION_CHANNEL,
            NotificationManagerCompat.IMPORTANCE_LOW
        ).setName(context.getString(R.string.timer_notification_channel_title))
            .setDescription(context.getString(R.string.timer_notification_channel_desc))
            .build()

        notificationManager.createNotificationChannel(flipperChannel)
    }
}

@DrawableRes
private fun ControlledTimerState.Running.getStatusImageId(): Int {
    return if (isOnPause) {
        when (this) {
            is ControlledTimerState.Running.Rest,
            is ControlledTimerState.Running.LongRest -> R.drawable.pic_status_rest_paused

            is ControlledTimerState.Running.Work -> R.drawable.pic_status_busy_paused
        }
    } else when (this) {
        is ControlledTimerState.Running.Rest,
        is ControlledTimerState.Running.LongRest -> R.drawable.pic_status_rest

        is ControlledTimerState.Running.Work -> R.drawable.pic_status_busy
    }
}