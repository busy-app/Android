package com.flipperdevices.bsb.dao.api

import com.flipperdevices.bsb.dao.model.TimerSettings
import com.flipperdevices.bsb.dao.model.cards.DBCardEntityMapper
import com.flipperdevices.bsb.dao.model.cards.DBCardRepository
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
    dbCardRepositoryProvider: KIProvider<DBCardRepository>
) : TimerSettingsApi {
    private val dbCardRepository by dbCardRepositoryProvider

    override fun getTimerSettingsFlow(): Flow<TimerSettings> {
        return dbCardRepository.getTimerSettingsFlow().map {
            DBCardEntityMapper.map(it)
        }
    }

    override suspend fun insert(settings: TimerSettings) = withContext(FlipperDispatchers.default) {
        dbCardRepository.insertOrUpdate(DBCardEntityMapper.map(settings))
    }
}