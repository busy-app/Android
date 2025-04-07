package com.flipperdevices.bsb.timer.statefactory.iteration.datetime

import kotlinx.datetime.Instant

fun interface TimeProvider {
    fun now(): Instant
}
