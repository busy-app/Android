package com.flipperdevices.bsb.timer.cards.composable

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import com.flipperdevices.bsb.dao.model.TimerSettings
import com.flipperdevices.ui.timeline.util.toFormattedTime

/**
 * [withCoercedAtMostAlpha] will not allow [alpha] be more than [Color.alpha]
 * Furthermore, alpha range will be resized [0;1] -> [0;0.4]
 * And the target value will also be moved 0.8 -> 0.32
 */
internal fun Color.withCoercedAtMostAlpha(alpha: Float): Color {
    val percent = this.alpha * alpha
    return copy(alpha = percent * alpha)
}

@Composable
internal fun PagerDescComposable(
    timerSettings: TimerSettings,
    alpha: Float
) {
    when (timerSettings.intervalsSettings.isEnabled) {
        true -> {
            Text(
                text = "Interval",
                color = LocalCorruptedPallet.current
                    .transparent
                    .whiteInvert
                    .secondary
                    .withCoercedAtMostAlpha(alpha = alpha),
                fontSize = 14.sp,
                fontFamily = LocalBusyBarFonts.current.jetbrainsMono
            )
            Text(
                text = "Work: ${timerSettings.intervalsSettings.work.toFormattedTime(slim = true)}",
                color = LocalCorruptedPallet.current
                    .transparent
                    .whiteInvert
                    .secondary
                    .withCoercedAtMostAlpha(alpha = alpha),
                fontSize = 14.sp,
                fontFamily = LocalBusyBarFonts.current.jetbrainsMono
            )
        }

        false -> {
            Text(
                text = "Simple",
                color = LocalCorruptedPallet.current
                    .transparent
                    .whiteInvert
                    .secondary
                    .withCoercedAtMostAlpha(alpha = alpha),
                fontSize = 14.sp,
                fontFamily = LocalBusyBarFonts.current.jetbrainsMono
            )
            Text(
                text = "Time: ${timerSettings.totalTime.toFormattedTime(slim = true)}",
                color = LocalCorruptedPallet.current
                    .transparent
                    .whiteInvert
                    .secondary
                    .withCoercedAtMostAlpha(alpha = alpha),
                fontSize = 14.sp,
                fontFamily = LocalBusyBarFonts.current.jetbrainsMono
            )
        }
    }
}
