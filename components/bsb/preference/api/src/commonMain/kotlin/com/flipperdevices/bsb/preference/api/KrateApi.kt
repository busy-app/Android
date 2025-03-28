package com.flipperdevices.bsb.preference.api

import ru.astrainteractive.klibs.kstorage.suspend.SuspendMutableKrate
import ru.astrainteractive.klibs.kstorage.suspend.flow.FlowMutableKrate

interface KrateApi {
    val firebaseTokenKrate: SuspendMutableKrate<String?>
}
