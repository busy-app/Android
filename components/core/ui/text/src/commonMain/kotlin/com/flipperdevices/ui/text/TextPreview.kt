package com.flipperdevices.ui.text

import BaselineText
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import com.flipperdevices.bsb.core.theme.LocalPallet
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
private fun FontPreview() {
    BusyBarThemeInternal(false) {
        val text = "Test"
        Column {
            listOf(
                LocalBusyBarFonts.current.pragmatica,
                LocalBusyBarFonts.current.ppNeueMontreal,
                MaterialTheme.typography.body1.fontFamily
            ).forEach { fontFamily ->
                Box(
                    Modifier
                        .background(LocalPallet.current.black.invert),
                ) {
                    var width by remember { mutableIntStateOf(0) }
                    Row(
                        Modifier.onGloballyPositioned {
                            width = it.size.width
                        },
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(16.dp)
                                .border(1.dp, Color.Red),
                            text = text,
                            fontFamily = fontFamily,
                            color = LocalPallet.current.white.invert,
                            fontSize = 16.sp,
                            lineHeight = 20.sp
                        )
                        BaselineText(
                            modifier = Modifier
                                .padding(16.dp)
                                .border(1.dp, Color.Red),
                            text = text,
                            fontFamily = fontFamily,
                            color = LocalPallet.current.white.invert,
                            fontSize = 16.sp,
                            lineHeight = 16.sp
                        )
                    }
                    Box(
                        Modifier
                            .align(Alignment.Center)
                            .width(LocalDensity.current.run { width.toDp() })
                            .height(1.dp)
                            .background(Color.Yellow)
                    )
                }
            }
        }
    }
}
