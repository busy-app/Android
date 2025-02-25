package com.flipperdevices.bsb.appblocker.stats.api

import com.flipperdevices.bsb.appblocker.stats.dao.AppStatsDatabase
import com.flipperdevices.bsb.appblocker.stats.dao.model.DBBlockedAppStat
import com.flipperdevices.bsb.appblocker.stats.model.AppLaunchRecordEvent
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.log.error
import com.flipperdevices.core.log.info
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.todayIn
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import kotlin.time.Duration.Companion.milliseconds

@Inject
@ContributesBinding(AppGraph::class, AppBlockerStatsApi::class)
class AppBlockerStatsApiImpl(
    private val appStatsDatabase: AppStatsDatabase
) : AppBlockerStatsApi, LogTagProvider {
    override val TAG = "AppBlockerStatsApi"

    override suspend fun recordBlockApp(
        event: AppLaunchRecordEvent
    ) {
        info { "Receive $event" }
        runCatching {
            appStatsDatabase.statsDao().insert(
                DBBlockedAppStat(
                    appPackage = event.appPackage,
                    timestampSeconds = event.timestamp.milliseconds.inWholeSeconds
                )
            )
        }.onSuccess {
            info { "Successfully recorded $event" }
        }.onFailure {
            error(it) { "Failed to add $event" }
        }
    }

    override suspend fun getBlockAppCount(packageName: String): Int {
        val countFromTimestampSeconds = Clock.System
            .todayIn(TimeZone.currentSystemDefault())
            .atStartOfDayIn(TimeZone.currentSystemDefault())
            .epochSeconds
        val count = appStatsDatabase.statsDao().getLaunchCount(
            appPackage = packageName,
            fromAtSeconds = countFromTimestampSeconds
        )
        appStatsDatabase.statsDao().clearRecordsBefore(
            countFromTimestampSeconds
        )
        return count
    }
}
