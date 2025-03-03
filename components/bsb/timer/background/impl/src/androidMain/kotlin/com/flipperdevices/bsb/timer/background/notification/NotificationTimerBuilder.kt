package com.flipperdevices.bsb.timer.background.notification

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.flipperdevices.bsb.timer.background.impl.R
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.background.notification.layout.CompactNotificationLayoutBuilder
import com.flipperdevices.bsb.timer.background.notification.layout.ExtendedNotificationLayoutBuilder
import com.flipperdevices.core.di.AndroidPlatformDependencies
import com.flipperdevices.core.log.LogTagProvider
import me.tatarka.inject.annotations.Inject

private const val TIMER_NOTIFICATION_CHANNEL = "timer_notification_channel"

@Inject
class NotificationTimerBuilder(
    private val platformDependencies: AndroidPlatformDependencies,
    private val compactNotificationLayoutBuilder: CompactNotificationLayoutBuilder,
    private val expandedNotificationLayoutBuilder: ExtendedNotificationLayoutBuilder
) : LogTagProvider {
    override val TAG = "NotificationTimerBuilder"

    fun buildStartUpNotification(
        context: Context
    ): Notification {
        return createBase(context)
            .setContentTitle(context.getString(R.string.timer_notification_title))
            .setContentText(context.getString(R.string.timer_notification_desc_empty))
            .build()
    }

    fun buildNotification(
        context: Context,
        timer: ControlledTimerState
    ): Notification? {
        if (timer !is ControlledTimerState.InProgress) {
            return null
        }

        val notificationBuilder = createBase(context)

        notificationBuilder.setCustomContentView(
            compactNotificationLayoutBuilder.getLayout(
                context,
                timer
            )
        )
        notificationBuilder.setCustomBigContentView(
            expandedNotificationLayoutBuilder.getLayout(
                context,
                timer
            )
        )

        return notificationBuilder.build()
    }

    private fun createBase(
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
