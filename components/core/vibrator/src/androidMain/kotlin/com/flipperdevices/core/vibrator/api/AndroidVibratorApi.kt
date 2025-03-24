package com.flipperdevices.core.vibrator.api

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.content.ContextCompat
import com.flipperdevices.core.di.AppGraph
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn
import kotlin.time.Duration

@Inject
@SingleIn(AppGraph::class)
@ContributesBinding(AppGraph::class, BVibratorApi::class)
class AndroidVibratorApi(private val context: Context) : BVibratorApi {
    override fun vibrateOnce(duration: Duration) {
        val vibrator = ContextCompat
            .getSystemService(context, Vibrator::class.java)
            ?: return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    duration.inWholeMilliseconds,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            // deprecated in API 26
            @Suppress("DEPRECATION")
            vibrator.vibrate(duration.inWholeMilliseconds)
        }
    }
}
