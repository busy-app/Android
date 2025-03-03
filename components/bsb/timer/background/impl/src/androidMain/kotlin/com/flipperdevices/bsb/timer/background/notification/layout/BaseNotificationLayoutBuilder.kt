package com.flipperdevices.bsb.timer.background.notification.layout

import android.content.Context
import android.view.View
import android.widget.RemoteViews
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import com.flipperdevices.bsb.timer.background.impl.R
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.background.model.toHumanReadableString

abstract class BaseNotificationLayoutBuilder(
    @LayoutRes
    private val layoutRes: Int
) {
    open fun getLayout(
        context: Context,
        timer: ControlledTimerState.InProgress
    ): RemoteViews {
        val notificationLayout = RemoteViews(
            context.packageName,
            layoutRes
        )

        when (timer) {
            is ControlledTimerState.InProgress.Await -> makeViewForAwait(
                context,
                notificationLayout,
                timer
            )

            is ControlledTimerState.InProgress.Running -> makeViewForRunning(
                notificationLayout,
                timer
            )
        }

        notificationLayout.setImageViewResource(
            R.id.status_pic,
            getStatusImageId(timer)
        )
        return notificationLayout
    }

    private fun makeViewForRunning(
        notificationLayout: RemoteViews,
        timer: ControlledTimerState.InProgress.Running
    ) {
        notificationLayout.setViewVisibility(R.id.timer, View.VISIBLE)
        notificationLayout.setViewVisibility(R.id.timer_status, View.GONE)
        notificationLayout.setViewVisibility(R.id.timer_icon, View.GONE)

        notificationLayout.setTextViewText(
            R.id.timer,
            timer.toHumanReadableString()
        )
    }

    private fun makeViewForAwait(
        context: Context,
        notificationLayout: RemoteViews,
        timer: ControlledTimerState.InProgress.Await
    ) {
        notificationLayout.setViewVisibility(R.id.timer, View.GONE)
        notificationLayout.setViewVisibility(R.id.timer_status, View.VISIBLE)
        notificationLayout.setViewVisibility(R.id.timer_icon, View.VISIBLE)

        val iconId = when (timer.type) {
            ControlledTimerState.InProgress.AwaitType.AFTER_WORK -> R.drawable.ic_checkmark
            ControlledTimerState.InProgress.AwaitType.AFTER_REST -> R.drawable.ic_laptop
        }
        notificationLayout.setImageViewResource(R.id.timer_icon, iconId)

        val text = when (timer.type) {
            ControlledTimerState.InProgress.AwaitType.AFTER_REST -> context.getString(
                R.string.timer_notification_after_rest
            )
            ControlledTimerState.InProgress.AwaitType.AFTER_WORK -> context.getString(
                R.string.timer_notification_after_busy,
                timer.currentIteration,
                timer.maxIterations
            )
        }
        notificationLayout.setTextViewText(R.id.timer_status, text)
    }
}

@DrawableRes
private fun getStatusImageId(timer: ControlledTimerState.InProgress): Int {
    return when (timer) {
        is ControlledTimerState.InProgress.Await ->
            when (timer.type) {
                ControlledTimerState.InProgress.AwaitType.AFTER_WORK -> R.drawable.pic_status_busy
                ControlledTimerState.InProgress.AwaitType.AFTER_REST -> R.drawable.pic_status_rest
            }

        is ControlledTimerState.InProgress.Running -> {
            if (timer.isOnPause) {
                when (timer) {
                    is ControlledTimerState.InProgress.Running.Rest,
                    is ControlledTimerState.InProgress.Running.LongRest -> R.drawable.pic_status_rest_paused

                    is ControlledTimerState.InProgress.Running.Work -> R.drawable.pic_status_busy_paused
                }
            } else {
                when (timer) {
                    is ControlledTimerState.InProgress.Running.Rest,
                    is ControlledTimerState.InProgress.Running.LongRest -> R.drawable.pic_status_rest

                    is ControlledTimerState.InProgress.Running.Work -> R.drawable.pic_status_busy
                }
            }
        }
    }
}
