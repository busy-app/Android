package com.flipperdevices.bsb.preference.api

import com.flipperdevices.bsb.preference.model.TimerSettings
import ru.astrainteractive.klibs.kstorage.suspend.flow.FlowMutableKrate

interface KrateApi {
    val timerSettingsKrate: FlowMutableKrate<TimerSettings>
}
