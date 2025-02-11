package com.flipperdevices.bsb.preference.api.internal

import com.flipperdevices.bsb.preference.api.KrateApi
import com.flipperdevices.bsb.preference.api.PreferenceApi
import com.flipperdevices.bsb.preference.model.SettingsEnum
import com.flipperdevices.bsb.preference.model.TimerSettings
import com.flipperdevices.core.di.AppGraph
import kotlinx.serialization.serializer
import me.tatarka.inject.annotations.Inject
import ru.astrainteractive.klibs.kstorage.api.value.ValueFactory
import ru.astrainteractive.klibs.kstorage.suspend.flow.FlowMutableKrate
import ru.astrainteractive.klibs.kstorage.suspend.impl.DefaultFlowMutableKrate
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppGraph::class)
@ContributesBinding(AppGraph::class, KrateApi::class)
class KrateApiImpl(
    private val preferenceApi: PreferenceApi
) : KrateApi {
    override val timerSettingsKrate: FlowMutableKrate<TimerSettings>
        get() {
            val factory = ValueFactory { TimerSettings() }
            return DefaultFlowMutableKrate(
                factory = factory,
                loader = {
                    preferenceApi.getFlowSerializable(
                        serializer = serializer<TimerSettings>(),
                        key = SettingsEnum.TIMER_SETTINGS,
                        default = factory.create()
                    )
                },
                saver = {
                    preferenceApi.setSerializable(
                        serializer = serializer<TimerSettings>(),
                        key = SettingsEnum.TIMER_SETTINGS,
                        value = it
                    )
                }
            )
        }
}
