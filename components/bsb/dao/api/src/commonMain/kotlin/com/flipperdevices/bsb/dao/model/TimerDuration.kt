package com.flipperdevices.bsb.dao.model

import kotlin.jvm.JvmInline
import kotlin.time.Duration

sealed interface TimerDuration {
    /**
     * Don't add duration for infinite! Use `when`!
     * In different cases the value could be different!
     * Example: Sometimes we need [Duration.ZERO] and sometimes [Duration.INFINITE]!
     */
    data object Infinite : TimerDuration

    @JvmInline
    value class Finite(val instance: Duration) : TimerDuration
}
