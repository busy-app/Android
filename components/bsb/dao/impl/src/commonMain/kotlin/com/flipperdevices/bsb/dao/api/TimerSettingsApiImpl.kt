package com.flipperdevices.bsb.dao.api

import com.flipperdevices.bsb.dao.model.AppDatabase
import com.flipperdevices.bsb.dao.model.TimerSettings
import com.flipperdevices.bsb.dao.model.TimerSettingsId
import com.flipperdevices.bsb.dao.model.cards.DBCardEntityMapper
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.di.KIProvider
import com.flipperdevices.core.di.provideDelegate
import com.flipperdevices.core.ktx.common.FlipperDispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(AppGraph::class, TimerSettingsApi::class)
class TimerSettingsApiImpl(
    databaseProvider: KIProvider<AppDatabase>
) : TimerSettingsApi {
    private val database by databaseProvider

    override fun getTimerSettingsListFlow(): Flow<List<TimerSettings>> {
        return database.cardRepository().getTimerSettingsListFlow().map { list ->
            list.map {
                DBCardEntityMapper.map(it)
            }
        }
    }

    override fun getTimerSettingsFlow(timerSettingsId: TimerSettingsId): Flow<TimerSettings?> {
        return database.cardRepository().getTimerSettingsFlow(timerSettingsId.id).map {
            if (it != null) {
                DBCardEntityMapper.map(it)
            } else {
                null
            }
        }
    }

    override suspend fun insert(
        settings: TimerSettings
    ): Unit = withContext(FlipperDispatchers.default) {
        database.cardRepository().insertOrUpdate(DBCardEntityMapper.map(settings))
    }
}
