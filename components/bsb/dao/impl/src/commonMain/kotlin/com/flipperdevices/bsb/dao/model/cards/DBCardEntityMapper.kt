package com.flipperdevices.bsb.dao.model.cards

import com.flipperdevices.bsb.dao.model.TimerDuration
import com.flipperdevices.bsb.dao.model.TimerSettings
import com.flipperdevices.bsb.dao.model.TimerSettingsId
import kotlin.time.Duration

object DBCardEntityMapper {
    fun map(entity: DBCardEntity): TimerSettings {
        return TimerSettings(
            id = TimerSettingsId(entity.id),
            name = entity.name,
            totalTime = entity.totalTime,
            intervalsSettings = TimerSettings.IntervalsSettings(
                work = entity.work,
                rest = entity.rest,
                longRest = entity.longRest,
                autoStartWork = entity.autoStartWork,
                autoStartRest = entity.autoStartRest,
                isEnabled = entity.isIntervalEnabled,
            ),
            soundSettings = TimerSettings.SoundSettings(
                alertWhenIntervalEnds = entity.alertWhenIntervalEnds
            )
        )
    }

    fun map(settings: TimerSettings): DBCardEntity {
        return DBCardEntity(
            id = settings.id.id,
            name = settings.name,
            totalTime = settings.totalTime,
            work = settings.intervalsSettings.work,
            rest = settings.intervalsSettings.rest,
            longRest = settings.intervalsSettings.longRest,
            autoStartWork = settings.intervalsSettings.autoStartWork,
            autoStartRest = settings.intervalsSettings.autoStartRest,
            isIntervalEnabled = settings.intervalsSettings.isEnabled,
            alertWhenIntervalEnds = settings.soundSettings.alertWhenIntervalEnds
        )
    }
}
