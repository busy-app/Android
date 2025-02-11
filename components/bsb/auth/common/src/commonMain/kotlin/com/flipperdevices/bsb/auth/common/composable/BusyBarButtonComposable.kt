package com.flipperdevices.bsb.auth.common.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flipperdevices.bsb.auth.common.composable.UiConstants.ALPHA_DISABLED
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import com.flipperdevices.bsb.core.theme.LocalPallet
import com.flipperdevices.core.ktx.common.clickableRipple
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun BusyBarButtonComposable(
    text: StringResource,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    inProgress: Boolean = false,
    disabled: Boolean = inProgress,
) {
    Row(
        modifier.clip(RoundedCornerShape(8.dp))
            .graphicsLayer {
                if (disabled) {
                    this.alpha = ALPHA_DISABLED
                }
            }
            .background(LocalPallet.current.accent.device.primary)
            .clickableRipple(onClick = {
                if (disabled.not()) {
                    onClick()
                }
            })
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(text),
            fontFamily = LocalBusyBarFonts.current.ppNeueMontreal,
            fontSize = 16.sp,
            fontWeight = FontWeight.W500,
            textAlign = TextAlign.Center,
            color = LocalPallet.current.white.onColor
        )
        if (inProgress) {
            CircularProgressIndicator(
                Modifier.size(22.dp)
                    .padding(1.2.dp),
                color = LocalPallet.current.white.onColor,
                backgroundColor = LocalPallet.current.transparent.whiteInvert.primary,
                strokeWidth = 1.dp
            )
        }
    }
}
