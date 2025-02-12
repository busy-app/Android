package com.flipperdevices.bsb.common

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import com.flipperdevices.bsb.core.theme.LocalPallet
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ProfileButtonComposable(
    text: StringResource,
    modifier: Modifier = Modifier,
    inProgress: Boolean = false,
) {
    Row(
        modifier
            .clip(RoundedCornerShape(112.dp))
            .background(LocalPallet.current.transparent.whiteInvert.tertiary)
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(text),
            fontSize = 16.sp,
            fontWeight = FontWeight.W500,
            fontFamily = LocalBusyBarFonts.current.ppNeueMontreal,
            color = LocalPallet.current.white.invert
        )

        if (inProgress) {
            CircularProgressIndicator(
                Modifier.size(20.dp)
                    .padding(start = 2.dp),
                color = LocalPallet.current.white.onColor,
                backgroundColor = LocalPallet.current.transparent.whiteInvert.primary,
                strokeWidth = 1.dp
            )
        }
    }
}
