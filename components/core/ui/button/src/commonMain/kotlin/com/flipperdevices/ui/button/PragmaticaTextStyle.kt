package com.flipperdevices.ui.button

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts

@Composable
fun PragmaticaTextStyle(): TextStyle {
    return TextStyle(
        lineHeight = 24.sp,
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Bottom,
            trim = LineHeightStyle.Trim.LastLineBottom
        ),
        fontFamily = LocalBusyBarFonts.current.pragmatica,
    )
}