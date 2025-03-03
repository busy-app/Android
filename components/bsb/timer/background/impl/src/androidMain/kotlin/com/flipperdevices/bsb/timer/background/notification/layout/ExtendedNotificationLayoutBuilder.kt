package com.flipperdevices.bsb.timer.background.notification.layout

import android.content.Context
import android.widget.RemoteViews
import com.flipperdevices.bsb.timer.background.impl.R
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.background.notification.TimerBroadcastReceiver
import com.flipperdevices.bsb.timer.background.service.TimerServiceActionEnum
import me.tatarka.inject.annotations.Inject

@Inject
class ExtendedNotificationLayoutBuilder :
    BaseNotificationLayoutBuilder(R.layout.notification_expanded) {
    override fun getLayout(
        context: Context,
        timer: ControlledTimerState.InProgress
    ): RemoteViews {
        val notificationLayout = super.getLayout(context, timer)

        val isOnPause = when (timer) {
            is ControlledTimerState.InProgress.Await -> true
            is ControlledTimerState.InProgress.Running -> timer.isOnPause
        }

        val iconId = when (isOnPause) {
            true -> R.drawable.ic_play
            false -> R.drawable.ic_pause
        }
        notificationLayout.setImageViewResource(R.id.btn_icon, iconId)

        val textId = when (timer) {
            is ControlledTimerState.InProgress.Await -> when (timer.type) {
                ControlledTimerState.InProgress.AwaitType.AFTER_WORK -> R.string.timer_notification_btn_play_after_busy
                ControlledTimerState.InProgress.AwaitType.AFTER_REST -> R.string.timer_notification_btn_play_after_rest
            }

            is ControlledTimerState.InProgress.Running -> when (timer.isOnPause) {
                true -> R.string.timer_notification_btn_play
                false -> R.string.timer_notification_btn_pause
            }
        }

        notificationLayout.setTextViewText(R.id.btn_text, context.getString(textId))

        val action = when (isOnPause) {
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
