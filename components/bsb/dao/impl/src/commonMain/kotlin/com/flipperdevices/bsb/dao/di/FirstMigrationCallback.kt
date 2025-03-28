package com.flipperdevices.bsb.dao.di

import androidx.room.RoomDatabase
import androidx.sqlite.SQLiteConnection
import com.flipperdevices.bsb.dao.model.AppDatabase
import com.flipperdevices.bsb.dao.model.TimerSettings
import com.flipperdevices.bsb.dao.model.TimerSettingsId
import com.flipperdevices.bsb.dao.model.cards.AUTOGENERATE_PRIMARY_ID
import com.flipperdevices.bsb.dao.model.cards.DBCardEntityMapper
import com.flipperdevices.bsb.preference.api.PreferenceApi
import com.flipperdevices.bsb.preference.api.get
import com.flipperdevices.bsb.preference.model.OldTimerSettings
import com.flipperdevices.bsb.preference.model.SettingsEnum
import com.flipperdevices.core.ktx.common.FlipperDispatchers
import com.flipperdevices.core.log.LogTagProvider
import com.flipperdevices.core.log.error
import com.flipperdevices.core.log.info
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.serializer

class FirstMigrationCallback(
    private val preferenceApi: PreferenceApi,
    private val scope: CoroutineScope
) : RoomDatabase.Callback(), LogTagProvider {
    override val TAG = "FirstMigrationCallback"

    private var dao: AppDatabase? = null

    fun setDAO(dao: AppDatabase) {
        this.dao = dao
    }

    override fun onCreate(connection: SQLiteConnection) {
        super.onCreate(connection)
        val appDatabase = dao
        info { "#onCreate database with $dao" }
        if (appDatabase == null) {
            error { "Failed migrate, because $dao is null" }
            return
        }

        scope.launch(FlipperDispatchers.default) {
            val oldTimerSettings = preferenceApi.getSerializable(
                serializer = serializer<OldTimerSettings>(),
                key = SettingsEnum.DEPRECATED_TIMER_SETTINGS,
                default = null
            )
            val isAppBlockingActive = preferenceApi.get(
                SettingsEnum.DEPRECATED_APP_BLOCKING,
                default = true
            )

            info { "Start make migration..." }

            val isSuccessful = runCatching {
                migration(appDatabase, oldTimerSettings, isAppBlockingActive)
            }.onFailure {
                error(it) { "Exception while apply migration" }
            }.getOrNull() ?: return@launch

            if (isSuccessful) {
                info { "Migration complete successful" }
            } else {
                error { "Failed apply migration" }
            }
        }
    }

    private suspend fun migration(
        dao: AppDatabase,
        oldSettings: OldTimerSettings?,
        isAppBlockingActive: Boolean
    ): Boolean {
        val newSettings = if (oldSettings == null) {
            TimerSettings(
                id = TimerSettingsId(AUTOGENERATE_PRIMARY_ID)
            )
        } else {
            TimerSettings(
                id = TimerSettingsId(AUTOGENERATE_PRIMARY_ID),
                totalTime = oldSettings.totalTime,
                intervalsSettings = TimerSettings.IntervalsSettings(
                    work = oldSettings.intervalsSettings.work,
                    rest = oldSettings.intervalsSettings.rest,
                    longRest = oldSettings.intervalsSettings.longRest,
                    autoStartWork = oldSettings.intervalsSettings.autoStartWork,
                    autoStartRest = oldSettings.intervalsSettings.autoStartRest,
                    isEnabled = oldSettings.intervalsSettings.isEnabled
                ),
                soundSettings = TimerSettings.SoundSettings(
                    alertWhenIntervalEnds = oldSettings.soundSettings.alertWhenIntervalEnds
                )
            )
        }

        val dbSettings = DBCardEntityMapper.map(newSettings)
        val newId = dao.cardRepository().insertOrUpdate(dbSettings)
        if (newId > 0) {
            dao.cardRepository().updateBlockedEnabled(
                cardId = newId,
                isBlockedEnabled = isAppBlockingActive
            )
            return true
        }

        return false
    }
}