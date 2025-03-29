package com.flipperdevices.bsb.preference.api

import ru.astrainteractive.klibs.kstorage.suspend.SuspendMutableKrate

interface KrateApi {
    val firebaseTokenKrate: SuspendMutableKrate<String?>
}
