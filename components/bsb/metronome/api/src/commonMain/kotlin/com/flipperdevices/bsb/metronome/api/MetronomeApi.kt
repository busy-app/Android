package com.flipperdevices.bsb.metronome.api

interface MetronomeApi {
    fun isMetronomeSupportActive(): Boolean
    fun enableSupport(): Result<Unit>
    fun disableSupport()
    fun play()
}
