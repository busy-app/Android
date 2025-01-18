package com.flipperdevices.bsb.metronome.api

import com.flipperdevices.bsb.preference.api.PreferenceApi
import com.flipperdevices.bsb.preference.api.get
import com.flipperdevices.bsb.preference.api.set
import com.flipperdevices.bsb.preference.model.SettingsEnum
import com.flipperdevices.core.di.AppGraph
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(AppGraph::class, MetronomeApi::class)
class MetronomeApiImpl(
    private val preferenceApi: PreferenceApi,
    private val audioPlayerApi: AudioPlayerApi
) : MetronomeApi {

    override fun isMetronomeSupportActive(): Boolean {
        return preferenceApi.get(SettingsEnum.METRONOME, false)
    }

    override fun enableSupport(): Result<Unit> {
        preferenceApi.set(SettingsEnum.METRONOME, true)
        return Result.success(Unit)
    }

    override fun disableSupport() {
        preferenceApi.set(SettingsEnum.METRONOME, false)
    }

    override fun play() {
        if (isMetronomeSupportActive()) {
            audioPlayerApi.play()
        }
    }
}
