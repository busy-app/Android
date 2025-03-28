package com.flipperdevices.bsb.preference.api.internal

import com.flipperdevices.bsb.preference.api.KrateApi
import com.flipperdevices.bsb.preference.api.PreferenceApi
import com.flipperdevices.bsb.preference.model.SettingsEnum
import com.flipperdevices.bsb.preference.model.OldTimerSettings
import com.flipperdevices.core.di.AppGraph
import com.russhwolf.settings.ObservableSettings
import kotlinx.serialization.serializer
import me.tatarka.inject.annotations.Inject
import ru.astrainteractive.klibs.kstorage.api.value.ValueFactory
import ru.astrainteractive.klibs.kstorage.suspend.SuspendMutableKrate
import ru.astrainteractive.klibs.kstorage.suspend.flow.FlowMutableKrate
import ru.astrainteractive.klibs.kstorage.suspend.impl.DefaultFlowMutableKrate
import ru.astrainteractive.klibs.kstorage.suspend.impl.DefaultSuspendMutableKrate
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppGraph::class)
@ContributesBinding(AppGraph::class, KrateApi::class)
class KrateApiImpl(
    private val preferenceApi: PreferenceApi,
    private val observableSettings: ObservableSettings
) : KrateApi {
    override val firebaseTokenKrate: SuspendMutableKrate<String?>
        get() {
            return DefaultSuspendMutableKrate(
                factory = { null },
                loader = { observableSettings.getStringOrNull(SettingsEnum.FIREBASE_TOKEN.key) },
                saver = { token ->
                    if (token.isNullOrEmpty()) {
                        observableSettings.remove(SettingsEnum.FIREBASE_TOKEN.key)
                    } else {
                        observableSettings.putString(SettingsEnum.FIREBASE_TOKEN.key, token)
                    }
                }
            )
        }
}
