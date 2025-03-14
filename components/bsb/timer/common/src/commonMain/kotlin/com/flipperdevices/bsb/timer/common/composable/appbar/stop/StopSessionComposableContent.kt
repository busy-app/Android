package com.flipperdevices.bsb.timer.common.composable.appbar.stop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import busystatusbar.components.bsb.timer.common.generated.resources.Res
import busystatusbar.components.bsb.timer.common.generated.resources.ic_stop
import busystatusbar.components.bsb.timer.common.generated.resources.ta_ask_stop_session
import busystatusbar.components.bsb.timer.common.generated.resources.ta_keep_focusing
import busystatusbar.components.bsb.timer.common.generated.resources.ta_stop
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import com.flipperdevices.ui.button.BChipButton
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.materials.ExperimentalHazeMaterialsApi
import dev.chrisbanes.haze.materials.HazeMaterials
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalHazeMaterialsApi::class)
@Composable
fun StopSessionComposableContent(
    hazeState: HazeState,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
            .navigationBarsPadding()
            .padding(vertical = 40.dp, horizontal = 16.dp)
            .clip(RoundedCornerShape(32.dp))
            .hazeEffect(
                hazeState,
                HazeMaterials.ultraThin(
                    containerColor = Color.Black.copy(alpha = 0.8f)
                )
            )
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(48.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = stringResource(Res.string.ta_ask_stop_session),
            fontSize = 18.sp,
            color = LocalCorruptedPallet.current
                .white
                .invert,
            fontWeight = FontWeight.W500,
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MinorActionButton(
                modifier = Modifier.weight(1f),
                onClick = onConfirm,
                text = Res.string.ta_stop,
                icon = Res.drawable.ic_stop,
                contentColor = LocalCorruptedPallet.current
                    .black
                    .invert,
                background = LocalCorruptedPallet.current
                    .white
                    .invert
            )
            MinorActionButton(
                modifier = Modifier.weight(1f),
                onClick = onDismiss,
                text = Res.string.ta_keep_focusing,
                icon = null,
                contentColor = LocalCorruptedPallet.current
                    .white
                    .invert,
                background = LocalCorruptedPallet.current
                    .transparent
                    .whiteInvert
                    .tertiary
            )
        }
    }
}

@Composable
private fun MinorActionButton(
    onClick: () -> Unit,
    text: StringResource,
    icon: DrawableResource?,
    contentColor: Color,
    background: Color,
    modifier: Modifier = Modifier,
) {
    BChipButton(
        modifier = modifier,
        onClick = onClick,
        fontSize = 20.sp,
        iconSize = 13.dp,
        spacedBy = 8.dp,
        contentPadding = PaddingValues(
            all = 16.dp
        ),
        painter = if (icon != null) {
            painterResource(icon)
        } else {
            null
        },
        text = stringResource(text),
        contentColor = contentColor,
        background = background,
    )
}

@Preview
@Composable
private fun StopSessionComposableContentPreview() {
    BusyBarThemeInternal {
        val hazeState = remember { HazeState() }
        StopSessionComposableContent(
            onDismiss = {},
            onConfirm = {},
            hazeState = hazeState
        )
    }
}
