package com.flipperdevices.bsb.auth.common.composable.subaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun AuthTextSubActionComposable(
    text: StringResource,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    inProgress: Boolean = false,
    disabled: Boolean = inProgress
) {
    Row(
        modifier
            .fillMaxWidth()
            .graphicsLayer {
                if (disabled) {
                    this.alpha = ALPHA_DISABLED
                }
            },
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(12.dp)
                .clickableRipple(onClick = {
                    if (inProgress.not()) {
                        onClick()
                    }
                }),
            text = stringResource(text),
            fontSize = 16.sp,
            fontFamily = LocalBusyBarFonts.current.ppNeueMontreal,
            fontWeight = FontWeight.W500,
            color = LocalPallet.current.accent.device.primary,
            textAlign = TextAlign.End
        )

        if (inProgress) {
            CircularProgressIndicator(
                Modifier
                    .size(22.dp)
                    .padding(1.2.dp),
                color = LocalPallet.current.accent.device.primary,
                backgroundColor = LocalPallet.current.accent.device.secondary,
                strokeWidth = 1.dp
            )
        }
    }
}
