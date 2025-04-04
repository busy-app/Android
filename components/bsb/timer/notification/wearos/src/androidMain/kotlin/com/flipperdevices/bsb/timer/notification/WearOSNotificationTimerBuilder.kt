package com.flipperdevices.bsb.timer.notification

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.wear.ongoing.OngoingActivity
import androidx.wear.ongoing.Status
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.background.model.toHumanReadableString
import com.flipperdevices.bsb.timer.notification.wearos.R
import com.flipperdevices.core.di.AndroidPlatformDependencies
import com.flipperdevices.core.di.AppGraph
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import com.flipperdevices.bsb.timer.notification.common.R as CommonR

@Inject
@ContributesBinding(AppGraph::class)
class WearOSNotificationTimerBuilder(
    private val commonNotificationTimerBuilder: CommonNotificationTimerBuilder,
    private val platformDependencies: AndroidPlatformDependencies
) : NotificationTimerBuilder {

    override fun buildStartUpNotification(
        context: Context
    ) = commonNotificationTimerBuilder.buildStartUpNotification(context)

    override fun buildNotification(
        context: Context,
        timer: ControlledTimerState,
        timerPendingIntents: TimerPendingIntents
    ): Notification? {
        if (timer !is ControlledTimerState.InProgress) {
            return null
        }

        val baseNotification = commonNotificationTimerBuilder.createBaseNotification(context)

        val description = getDescription(context, timer)

        baseNotification.setContentTitle(context.getString(CommonR.string.timer_notification_title))
            .setContentText(description)
            .setSmallIcon(R.drawable.ic_notification_wearos_busy_logo)

        val ongoingActivityStatus = Status.Builder()
            // Sets the text used across various surfaces.
            .addTemplate(description)
            .build()

        val ongoingActivity = OngoingActivity.Builder(
            context, ONGOING_NOTIFICATION_ID, baseNotification
        ).setStaticIcon(R.drawable.ic_notification_wearos_busy_logo)
            .setTouchIntent(
                PendingIntent.getActivity(
                    context,
                    0,
                    Intent(context, platformDependencies.splashScreenActivity.java),
                    PendingIntent.FLAG_IMMUTABLE
                )
            )
            .setStatus(ongoingActivityStatus)
            .build()

        ongoingActivity.apply(context)

        return baseNotification.build()
    }
}

private fun getDescription(context: Context, timer: ControlledTimerState.InProgress) =
    when (timer) {
        is ControlledTimerState.InProgress.Await -> when (timer.type) {
            ControlledTimerState.InProgress.AwaitType.AFTER_REST -> context.getString(
                CommonR.string.timer_notification_after_rest
            )

            ControlledTimerState.InProgress.AwaitType.AFTER_WORK -> context.getString(
                CommonR.string.timer_notification_after_busy,
                timer.currentIteration,
                timer.maxIterations
            )
        }

        is ControlledTimerState.InProgress.Running.LongRest ->
            context.getString(
                R.string.timer_notification_status_long_rest,
                timer.toHumanReadableString()
            )

        is ControlledTimerState.InProgress.Running.Rest ->
            context.getString(
                R.string.timer_notification_status_rest,
                timer.toHumanReadableString()
            )

        is ControlledTimerState.InProgress.Running.Work ->
            context.getString(
                R.string.timer_notification_status_busy,
                timer.toHumanReadableString()
            )

    }