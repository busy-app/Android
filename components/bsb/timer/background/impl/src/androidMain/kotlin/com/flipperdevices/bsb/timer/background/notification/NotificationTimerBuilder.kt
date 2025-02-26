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
import com.flipperdevices.bsb.timer.background.service.TimerServiceActionEnum
import com.flipperdevices.core.log.LogTagProvider

private const val TIMER_NOTIFICATION_CHANNEL = "timer_notification_channel"

object NotificationTimerBuilder : LogTagProvider {
    override val TAG = "NotificationTimerBuilder"

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

        notificationBuilder.setCustomContentView(getCompactLayout(context, timer))
        notificationBuilder.setCustomBigContentView(getExpandedLayout(context, timer))

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

    private fun getCompactLayout(
        context: Context,
        timer: ControlledTimerState.Running
    ): RemoteViews {
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
        return notificationLayout
    }

    private fun getExpandedLayout(
        context: Context,
        timer: ControlledTimerState.Running
    ): RemoteViews {
        val notificationLayout = RemoteViews(
            context.packageName,
            R.layout.notification_expanded
        )
        notificationLayout.setTextViewText(
            R.id.timer,
            timer.toHumanReadableString()
        )
        notificationLayout.setImageViewResource(
            R.id.status_pic,
            timer.getStatusImageId()
        )

        val iconId = when (timer.isOnPause) {
            true -> R.drawable.ic_play
            false -> R.drawable.ic_pause
        }
        notificationLayout.setImageViewResource(R.id.btn_icon, iconId)

        val textId = when (timer.isOnPause) {
            true -> R.string.timer_notification_btn_play
            false -> R.string.timer_notification_btn_pause
        }
        notificationLayout.setTextViewText(R.id.btn_text, context.getString(textId))

        val action = when (timer.isOnPause) {
            true -> TimerServiceActionEnum.RESUME
            false -> TimerServiceActionEnum.PAUSE
        }

        notificationLayout.setOnClickPendingIntent(
            R.id.btn,
            TimerBroadcastReceiver.getTimerIntent(context, action)
        )

        return notificationLayout
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
    } else {
        when (this) {
            is ControlledTimerState.Running.Rest,
            is ControlledTimerState.Running.LongRest -> R.drawable.pic_status_rest

            is ControlledTimerState.Running.Work -> R.drawable.pic_status_busy
        }
    }
}
