package com.flipperdevices.bsbwearable.interrupt.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import busystatusbar.components.bsb.timer.common.generated.resources.ic_stop
import busystatusbar.instances.bsb_wear.generated.resources.Res
import busystatusbar.instances.bsb_wear.generated.resources.bwin_keep
import busystatusbar.instances.bsb_wear.generated.resources.bwin_stop
import busystatusbar.instances.bsb_wear.generated.resources.bwin_stop_desc
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import com.flipperdevices.ui.button.BChipButton
import com.google.android.horologist.compose.layout.ScalingLazyColumn
import com.google.android.horologist.compose.layout.ScalingLazyColumnDefaults
import com.google.android.horologist.compose.layout.ScreenScaffold
import com.google.android.horologist.compose.layout.rememberResponsiveColumnState
import com.google.android.horologist.compose.material.ListHeaderDefaults.firstItemPadding
import com.google.android.horologist.compose.material.ListHeaderDefaults.itemPadding
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import busystatusbar.components.bsb.timer.common.generated.resources.Res as CommonRes

@Composable
internal fun ConfirmStopOverlayComposable(
    onStopClick: () -> Unit,
    onDismiss: () -> Unit
) {
    BWearDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        ),
        content = {
            ConfirmStopOverlayContentComposable(
                onStopClick = onStopClick,
                onDismiss = onDismiss
            )
        }
    )
}

@Composable
@Suppress("LongMethod")
private fun ConfirmStopOverlayContentComposable(
    onStopClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val columnState = rememberResponsiveColumnState(
        contentPadding = ScalingLazyColumnDefaults.padding(
            first = ScalingLazyColumnDefaults.ItemType.Text,
            last = ScalingLazyColumnDefaults.ItemType.SingleButton
        )
    )
    ScreenScaffold(
        modifier = modifier,
        scrollState = columnState
    ) {
        ScalingLazyColumn(
            columnState = columnState
        ) {
            item {
                Text(
                    modifier = Modifier.padding(firstItemPadding()),
                    text = stringResource(Res.string.bwin_stop_desc),
                    color = LocalCorruptedPallet.current
                        .white
                        .onColor,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }
            item {
                BChipButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(itemPadding()),
                    onClick = onStopClick,
                    iconSize = 12.dp,
                    spacedBy = 8.dp,
                    text = stringResource(Res.string.bwin_stop),
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
            item {
                BChipButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(itemPadding()),
                    onClick = onDismiss,
                    text = stringResource(Res.string.bwin_keep),
                    painter = null,
                    contentColor = LocalCorruptedPallet.current.white.onColor,
                    background = Color(color = 0x1AFFFFFF), // todo
                    fontSize = 16.sp,
                    iconSize = 12.dp,
                    spacedBy = 8.dp,
                    contentPadding = PaddingValues(
                        vertical = 10.dp,
                        horizontal = 14.dp
                    ),
                    maxLines = Int.MAX_VALUE
                )
            }
        }
    }
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
