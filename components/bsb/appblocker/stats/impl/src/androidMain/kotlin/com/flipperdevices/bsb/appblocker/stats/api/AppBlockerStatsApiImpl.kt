package com.flipperdevices.bsb.appblocker.stats.api

import com.flipperdevices.bsb.appblocker.stats.dao.AppStatsDatabase
import com.flipperdevices.bsb.appblocker.stats.dao.model.DBBlockedAppStat
import com.flipperdevices.bsb.appblocker.stats.model.AppLaunchRecordEvent
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.log.error
import com.flipperdevices.core.log.info
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(AppGraph::class, AppBlockerStatsApi::class)
class AppBlockerStatsApiImpl(
    private val scope: CoroutineScope,
    private val appStatsDatabase: AppStatsDatabase
) : AppBlockerStatsApi, LogTagProvider {
    override val TAG = "AppBlockerStatsApi"

    override fun recordBlockAppAsync(event: AppLaunchRecordEvent) {
        info { "Receive $event" }
        scope.launch {
            runCatching {
                appStatsDatabase.statsDao().insert(
                    DBBlockedAppStat(
                        appPackage = event.appPackage,
                        timestamp = event.timestamp
                    )
                )
            }.onSuccess {
                info { "Successfully recorded $event" }
            }.onFailure {
                error(it) { "Failed to add $event" }
            }
        }
    }

    override suspend fun getBlockAppCount(packageName: String): Int {
        val countFromTimestampMs = Clock.System
            .todayIn(TimeZone.currentSystemDefault())
            .atStartOfDayIn(TimeZone.currentSystemDefault())
            .toEpochMilliseconds()
        val count = appStatsDatabase.statsDao().getLaunchCount(
            appPackage = packageName,
            fromAt = countFromTimestampMs
        )
        appStatsDatabase.statsDao().clearRecordsBefore(countFromTimestampMs)
        return count
    }
}