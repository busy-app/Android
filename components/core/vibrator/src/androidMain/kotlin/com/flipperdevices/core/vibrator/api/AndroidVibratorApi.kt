package com.flipperdevices.core.vibrator.api

import android.content.Context
import android.os.Build
import android.os.VibrationAttributes
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.flipperdevices.core.di.AppGraph
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import kotlin.time.Duration

private val VIBRATE_PATTERN = longArrayOf(500, 500)


@Inject
@ContributesBinding(AppGraph::class, BVibratorApi::class)
class AndroidVibratorApi(
    private val context: Context,
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
                    var composition = VibrationEffect.startComposition()

                    repeat(10) {
                        composition = composition
                            .addPrimitive(VibrationEffect.Composition.PRIMITIVE_THUD)
                    }
                    composition.compose()


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        vibrator.vibrate(
                            composition
                                .compose(),
                            0,
                            VibrationAttributes.createForUsage(VibrationAttributes.USAGE_ALARM),
                        )
                    } else {
                        vibrator.vibrate(
                            composition
                                .compose(),
                        )
                    }
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