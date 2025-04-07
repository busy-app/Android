package com.flipperdevices.bsb.timer.notification

import android.app.Notification
import android.content.Context
import com.flipperdevices.bsb.timer.background.model.ControlledTimerState
import com.flipperdevices.bsb.timer.notification.layout.CompactNotificationLayoutBuilder
import com.flipperdevices.bsb.timer.notification.layout.ExtendedNotificationLayoutBuilder
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.log.LogTagProvider
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(AppGraph::class, NotificationTimerBuilder::class)
class AndroidNotificationTimerBuilderImpl(
    private val commonNotificationTimerBuilder: CommonNotificationTimerBuilder,
    private val compactNotificationLayoutBuilder: CompactNotificationLayoutBuilder,
    private val expandedNotificationLayoutBuilder: ExtendedNotificationLayoutBuilder
) : NotificationTimerBuilder, LogTagProvider {
    override val TAG = "NotificationTimerBuilder"

    override fun buildStartUpNotification(
        context: Context
    ) = commonNotificationTimerBuilder.buildStartUpNotification(context)
        .build()

    override fun buildNotification(
        context: Context,
        timer: ControlledTimerState,
        timerPendingIntents: TimerPendingIntents
    ): Notification? {
        if (timer !is ControlledTimerState.InProgress) {
            return null
        }

        val notificationBuilder = commonNotificationTimerBuilder.createBaseNotification(context)

        notificationBuilder.setCustomContentView(
            compactNotificationLayoutBuilder.getLayout(
                context,
                timer,
                timerPendingIntents
            )
        )
        notificationBuilder.setCustomBigContentView(
            expandedNotificationLayoutBuilder.getLayout(
                context,
                timer,
                timerPendingIntents
            )
        )

        return notificationBuilder.build()
    }
}
