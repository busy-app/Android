package com.flipperdevices.bsb.timer.statefactory.iteration

import com.flipperdevices.bsb.dao.model.TimerSettings
import com.flipperdevices.bsb.timer.statefactory.iteration.model.IterationData
import kotlin.time.Duration

interface IterationBuilder {
    fun build(settings: TimerSettings, duration: Duration): List<IterationData>
}
