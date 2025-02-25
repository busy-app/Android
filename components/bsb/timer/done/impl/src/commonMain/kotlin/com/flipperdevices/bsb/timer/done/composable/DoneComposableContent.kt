package com.flipperdevices.bsb.timer.done.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import busystatusbar.components.bsb.timer.done.impl.generated.resources.Res
import busystatusbar.components.bsb.timer.done.impl.generated.resources.td_finish
import busystatusbar.components.bsb.timer.done.impl.generated.resources.td_finished_card
import busystatusbar.components.bsb.timer.done.impl.generated.resources.td_restart
import busystatusbar.components.bsb.timer.done.impl.generated.resources.td_well_done
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import com.flipperdevices.ui.button.BChipButton
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Suppress("LongMethod")
@Composable
fun DoneComposableContent(
    onFinishClick: () -> Unit,
    onRestartClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(color = 0xFF000000), // todo
                            Color(color = 0xFF0E1448), // todo
                        )
                    )
                )
        )
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 24.dp)
                .padding(vertical = 54.dp)
                .statusBarsPadding()
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(Res.string.td_well_done),
                    fontWeight = FontWeight.W500,
                    color = LocalCorruptedPallet.current
                        .white
                        .invert,
                    fontSize = 40.sp
                )
                Text(
                    text = stringResource(Res.string.td_finished_card),
                    fontWeight = FontWeight.W500,
                    color = LocalCorruptedPallet.current
                        .white
                        .invert,
                    fontSize = 18.sp
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BChipButton(
                    onClick = onFinishClick,
                    text = stringResource(Res.string.td_finish),
                    fontSize = 18.sp,
                    painter = null,
                    background = LocalCorruptedPallet.current
                        .transparent
                        .whiteInvert
                        .tertiary,
                    contentPadding = PaddingValues(
                        horizontal = 64.dp,
                        vertical = 16.dp
                    ),
                    contentColor = LocalCorruptedPallet.current
                        .white
                        .invert
                )

                BChipButton(
                    onClick = onRestartClick,
                    text = stringResource(Res.string.td_restart),
                    fontSize = 18.sp,
                    painter = null,
                    background = Color.Transparent,
                    contentPadding = PaddingValues(
                        horizontal = 64.dp,
                        vertical = 16.dp
                    ),
                    contentColor = LocalCorruptedPallet.current
                        .transparent
                        .whiteInvert
                        .primary
                )
            }
        }
    }
}

@Preview
@Composable
private fun DoneComposableContentPreview() {
    BusyBarThemeInternal {
        DoneComposableContent(
            onFinishClick = {},
            onRestartClick = {}
        )
    }
}
