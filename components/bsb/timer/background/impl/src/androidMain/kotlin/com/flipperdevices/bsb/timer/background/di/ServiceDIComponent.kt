package com.flipperdevices.bsb.timer.background.di

import com.flipperdevices.bsb.timer.background.api.CommonTimerApi
import com.flipperdevices.bsb.timer.background.notification.NotificationTimerBuilder
import com.flipperdevices.core.di.AppGraph
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo

@ContributesTo(AppGraph::class)
interface ServiceDIComponent {
    val commonTimerApi: CommonTimerApi
    val notificationBuilder: NotificationTimerBuilder
}
