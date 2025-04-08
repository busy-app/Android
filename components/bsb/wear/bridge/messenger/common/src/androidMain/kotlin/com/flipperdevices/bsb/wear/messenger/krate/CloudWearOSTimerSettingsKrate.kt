package com.flipperdevices.bsb.wear.messenger.krate

import com.flipperdevices.bsb.wear.messenger.model.WearOSTimerSettings
import kotlinx.collections.immutable.ImmutableList
import ru.astrainteractive.klibs.kstorage.suspend.flow.FlowMutableKrate

interface CloudWearOSTimerSettingsKrate : FlowMutableKrate<ImmutableList<WearOSTimerSettings>>