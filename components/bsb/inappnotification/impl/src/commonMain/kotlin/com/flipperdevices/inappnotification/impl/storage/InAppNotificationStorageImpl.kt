package com.flipperdevices.inappnotification.impl.storage

import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.ktx.common.withLock
import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.log.info
import com.flipperdevices.inappnotification.api.InAppNotificationListener
import com.flipperdevices.inappnotification.api.InAppNotificationStorage
import com.flipperdevices.inappnotification.api.model.InAppNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.sync.Mutex
import kotlinx.datetime.Clock
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn
import kotlin.time.Duration.Companion.seconds

private val TIMER_DELAY = 1.seconds

@Inject
@SingleIn(AppGraph::class)
@ContributesBinding(AppGraph::class, InAppNotificationStorage::class)
class InAppNotificationStorageImpl :
    InAppNotificationStorage,
    LogTagProvider {
    override val TAG = "InAppNotificationStorage"
    private val coroutineScope = CoroutineScope(SupervisorJob())
    private val timerTask = TimerTask(
        delayDuration = TIMER_DELAY,
        coroutineScope = coroutineScope,
        block = { invalidate() }
    )

    private val pendingNotification = ArrayDeque<InAppNotification>()
    private var listener: InAppNotificationListener? = null
    private var nextNotificationTime = 0L
    private var mutex = Mutex()

    override suspend fun subscribe(listener: InAppNotificationListener) =
        withLock(mutex, "subscribe") {
            info { "#subscribe. Current listener: ${this.listener}, updated: $listener" }
            if (this.listener != null) {
                unsubscribe()
            }
            this.listener = listener
            timerTask.start()
            invalidate()
        }

    override suspend fun unsubscribe() {
        info { "#unsubscribe. Current listener: ${this.listener}" }
        this.listener = null
        timerTask.shutdown()
    }

    override suspend fun addNotification(notification: InAppNotification) = withLock(mutex, "add") {
        pendingNotification.addLast(notification)
        invalidate()
    }

    private suspend fun invalidate() {
        val notificationListener = listener ?: return

        val currentTime = Clock.System.now().toEpochMilliseconds()
        if (currentTime < nextNotificationTime) {
            return
        }

        if (pendingNotification.isEmpty()) {
            return
        }

        val notificationToShown = pendingNotification.removeAt(0)
        notificationListener.onNewNotification(notificationToShown)
        nextNotificationTime = Clock.System.now().toEpochMilliseconds() +
            notificationToShown.duration.inWholeMilliseconds
    }
}
