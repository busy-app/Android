package com.flipperdevices.bsb.timer.cards.composable

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import busystatusbar.components.bsb.timer.cards.impl.generated.resources.Res
import busystatusbar.components.bsb.timer.cards.impl.generated.resources.ic_modal_arrow_up
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun OptionsComposable() {
    val infiniteTransition = rememberInfiniteTransition()
    val offset: Float by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000),
            repeatMode = RepeatMode.Reverse
        )
    )
    Icon(
        painter = painterResource(Res.drawable.ic_modal_arrow_up),
        modifier = Modifier.width(38.dp)
            .offset(y = offset.dp),
        tint = LocalCorruptedPallet.current
            .transparent
            .whiteInvert
            .secondary,
        contentDescription = null
    )
    Spacer(Modifier.height(12.dp))
    Text(
        text = "Options",
        color = LocalCorruptedPallet.current
            .transparent
            .whiteInvert
            .secondary,
        fontSize = 14.sp,
        fontFamily = LocalBusyBarFonts.current.jetbrainsMono
    )
}
