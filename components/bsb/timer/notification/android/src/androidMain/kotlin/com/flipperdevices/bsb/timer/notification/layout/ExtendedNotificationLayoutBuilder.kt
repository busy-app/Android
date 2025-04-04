package com.flipperdevices.bsb.timer.notification.layout

import android.content.Context
import android.widget.RemoteViews
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.notification.TimerPendingIntents
import com.flipperdevices.bsb.timer.notification.android.R
import me.tatarka.inject.annotations.Inject

@Inject
class ExtendedNotificationLayoutBuilder :
    BaseNotificationLayoutBuilder(R.layout.notification_expanded) {
    @Suppress("CyclomaticComplexMethod")
    override fun getLayout(
        context: Context,
        timer: ControlledTimerState.InProgress,
        intents: TimerPendingIntents
    ): RemoteViews {
        val notificationLayout = super.getLayout(context, timer, intents)

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
        when (timer) {
            is ControlledTimerState.InProgress.Await -> when (timer.type) {
                ControlledTimerState.InProgress.AwaitType.AFTER_REST,
                ControlledTimerState.InProgress.AwaitType.AFTER_WORK -> {
                    notificationLayout.setOnClickPendingIntent(
                        R.id.btn_text,
                        intents.nextStep
                    )
                }
            }

            else -> Unit
        }
        notificationLayout.setTextViewText(R.id.btn_text, context.getString(textId))

        val intent = when (isOnPause) {
            true -> intents.resume
            false -> intents.pause
        }

        notificationLayout.setOnClickPendingIntent(
            R.id.btn,
            intent
        )

        return notificationLayout
    }
}
