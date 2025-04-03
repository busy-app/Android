package com.flipperdevices.bsb.timer.background.newstatefactory.iteration

import com.flipperdevices.bsb.dao.model.TimerSettings
import com.flipperdevices.bsb.timer.background.newstatefactory.iteration.model.IterationData
import kotlin.time.Duration

interface IterationBuilder {
    fun build(settings: TimerSettings, duration: Duration): List<IterationData>
}
