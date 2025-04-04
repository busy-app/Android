package com.flipperdevices.bsb.timer.notification

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.flipperdevices.bsb.timer.notification.common.R
import com.flipperdevices.core.di.AndroidPlatformDependencies
import me.tatarka.inject.annotations.Inject

private const val TIMER_NOTIFICATION_CHANNEL = "timer_notification_channel"

@Inject
class CommonNotificationTimerBuilder(
    private val platformDependencies: AndroidPlatformDependencies
) {
    fun buildStartUpNotification(
        context: Context
    ): Notification {
        return createBaseNotification(context)
            .setContentTitle(context.getString(R.string.timer_notification_title))
            .setContentText(context.getString(R.string.timer_notification_desc_empty))
            .build()
    }

    fun createBaseNotification(
        context: Context
    ): NotificationCompat.Builder {
        createChannelIfNotYet(context)
        return NotificationCompat.Builder(context, TIMER_NOTIFICATION_CHANNEL)
            .setSilent(true)
            .setSmallIcon(R.drawable.ic_busy_logo)
            .setColor(context.getColor(R.color.brand_color))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setChannelId(TIMER_NOTIFICATION_CHANNEL)
            .setContentIntent(
                PendingIntent.getActivity(
                    context,
                    0,
                    Intent(context, platformDependencies.splashScreenActivity.java),
                    PendingIntent.FLAG_IMMUTABLE
                )
            )
            .setOngoing(true)
            .setLocalOnly(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
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