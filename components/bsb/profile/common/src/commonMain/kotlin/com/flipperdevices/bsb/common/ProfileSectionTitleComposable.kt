package com.flipperdevices.bsb.common

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import com.flipperdevices.bsb.core.theme.LocalPallet
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ProfileSectionTitleComposable(
    title: StringResource,
    modifier: Modifier = Modifier
) {
    Row(modifier) {
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(title),
            fontSize = 24.sp,
            fontWeight = FontWeight.W500,
            fontFamily = LocalBusyBarFonts.current.ppNeueMontreal,
            color = LocalPallet.current.white.invert
        )
    }
}
