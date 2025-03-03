package com.flipperdevices.bsbwearable.interrupt.composable


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import busystatusbar.components.bsb.timer.common.generated.resources.Res
import busystatusbar.components.bsb.timer.common.generated.resources.ic_play
import busystatusbar.components.bsb.timer.common.generated.resources.ic_stop
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import com.flipperdevices.ui.button.BChipButton
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.compose.layout.ScalingLazyColumn
import com.google.android.horologist.compose.layout.fillMaxRectangle
import com.google.android.horologist.compose.layout.rememberColumnState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import busystatusbar.components.bsb.timer.common.generated.resources.Res as CommonRes


@OptIn(ExperimentalHorologistApi::class)
@Composable
internal fun ConfirmStopOverlayComposable(
    onStopClick: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        ),
        content = {
            ScalingLazyColumn(
                modifier = Modifier.fillMaxSize(),
                columnState = rememberColumnState()
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                item {
                    Text(
                        text = "Stopping will reset this BUSY progress. Are you sure?",
                        color = LocalCorruptedPallet.current
                            .white
                            .onColor,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
                item { Spacer(Modifier.height(8.dp)) }
                item {
                    BChipButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onStopClick,
                        text = "Stop",
                        painter = painterResource(CommonRes.drawable.ic_stop),
                        contentColor = LocalCorruptedPallet.current.black.onColor,
                        background = LocalCorruptedPallet.current.white.onColor,
                        fontSize = 16.sp,
                        contentPadding = PaddingValues(
                            vertical = 10.dp,
                            horizontal = 14.dp
                        )
                    )
                }
                item { Spacer(Modifier.height(2.dp)) }
                item {
                    BChipButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onDismiss,
                        text = "Keep BUSY",
                        painter = null,
                        contentColor = LocalCorruptedPallet.current.white.onColor,
                        background = Color(0x1AFFFFFF),
                        fontSize = 16.sp,
                        contentPadding = PaddingValues(
                            vertical = 10.dp,
                            horizontal = 14.dp
                        )
                    )
                }
            }
        }
    )
}

@Preview
@Composable
private fun PreviewConfirmStopOverlayComposable() {
    BusyBarThemeInternal {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Red)
        )
        ConfirmStopOverlayComposable(
            onStopClick = {},
            onDismiss = {}
        )
    }
}
