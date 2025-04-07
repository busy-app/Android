package com.flipperdevices.bsb.timer.notification

import android.app.PendingIntent

data class TimerPendingIntents(
    val nextStep: PendingIntent,
    val resume: PendingIntent,
    val pause: PendingIntent
)
