package com.flipperdevices.core.vibrator.api

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

class AndroidVibratorApi(
    private val context: Context
) : BVibratorApi {
    @Suppress("DEPRECATION")
    override fun vibrateOnce(vibrateMode: VibrateMode) {
        val vibrator = ContextCompat
            .getSystemService(context, Vibrator::class.java)
            ?: return

        when (vibrateMode) {
            VibrateMode.TICK -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK))
            } else {
                vibrator.vibrate(vibrateMode.duration.inWholeMilliseconds)
            }

            VibrateMode.THUD -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                    isPrimitiveSupported(
                        vibrator,
                        VibrationEffect.Composition.PRIMITIVE_THUD
                    )
                ) {
                    vibrator.vibrate(
                        VibrationEffect.startComposition()
                            .addPrimitive(VibrationEffect.Composition.PRIMITIVE_THUD)
                            .compose()
                    )
                } else {
                    vibrator.vibrate(vibrateMode.duration.inWholeMilliseconds)
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.R)
private fun isPrimitiveSupported(vibrator: Vibrator, primitiveId: Int): Boolean {
    return vibrator.areAllPrimitivesSupported(primitiveId)
}