package com.flipperdevices.bsb.appblocker.listener

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import com.flipperdevices.bsb.appblocker.deeplink.AppBlockDeeplinkParser
import com.flipperdevices.bsb.appblocker.stats.api.AppBlockerStatsApi
import com.flipperdevices.bsb.appblocker.stats.model.AppLaunchRecordEvent
import com.flipperdevices.core.di.AndroidPlatformDependencies
import com.flipperdevices.core.ktx.common.withLock
import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.log.info
import com.flipperdevices.core.log.verbose
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import me.tatarka.inject.annotations.Inject

const val APP_LOCK_LOOP_INTERVAL = 500L
const val APP_LOCK_CHECK_INTERVAL = 1000L

@Inject
class UsageStatsLooper(
    private val context: Context,
    private val scope: CoroutineScope,
    private val androidPlatformDependencies: AndroidPlatformDependencies,
    private val packageFilter: PackageFilter,
    private val appBlockerStatsApi: AppBlockerStatsApi
) : LogTagProvider {
    override val TAG = "UsageStatsLooper"

    private val statsManager by lazy { context.getSystemService(UsageStatsManager::class.java) }
    private val mutex = Mutex()
    private var job: Job? = null

    fun startLoop() = scope.launch {
        withLock(mutex, "start") {
            job?.cancelAndJoin()
            job = scope.launch {
                while (isActive) {
                    loop()
                    delay(APP_LOCK_LOOP_INTERVAL)
                }
            }
        }
    }

    fun stopLoop() = scope.launch {
        withLock(mutex, "stop") {
            job?.cancelAndJoin()
            job = null
        }
    }

    private suspend fun loop() {
        val now = System.currentTimeMillis()
        val events = statsManager.queryEvents(now - APP_LOCK_CHECK_INTERVAL, now)

        val event = UsageEvents.Event()
        while (events.hasNextEvent()) {
            events.getNextEvent(event)
            verbose { "Process $event" }
            checkEvent(event)
        }
    }

    private suspend fun checkEvent(event: UsageEvents.Event) {
        if (event.eventType != UsageEvents.Event.ACTIVITY_RESUMED) {
            return // Not activity event
        }

        if (packageFilter.isForbidden(event.packageName).not()) {
            return
        }

        info { "Detect forbidden app in event with type ${event.eventType}: $event" }

        appBlockerStatsApi.recordBlockApp(
            event = AppLaunchRecordEvent(
                appPackage = event.packageName,
                timestamp = event.timeStamp
            )
        )

        val openCount = appBlockerStatsApi.getBlockAppCount(
            event.packageName
        )

        val intent = AppBlockDeeplinkParser.getIntent(
            context = context,
            packageName = event.packageName,
            openCount = openCount,
            activity = androidPlatformDependencies.splashScreenActivity,
        )
        context.startActivity(intent)
    }
}
