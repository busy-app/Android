package com.flipperdevices.bsbwearable.finish.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import busystatusbar.instances.bsb_wear.generated.resources.Res
import busystatusbar.instances.bsb_wear.generated.resources.bwfi_action
import busystatusbar.instances.bsb_wear.generated.resources.bwfi_desc
import busystatusbar.instances.bsb_wear.generated.resources.bwfi_title
import busystatusbar.instances.bsb_wear.generated.resources.ic_checkmark
import busystatusbar.instances.bsb_wear.generated.resources.ic_reload
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import com.flipperdevices.ui.button.BChipButton
import com.flipperdevices.ui.button.BIconButton
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Suppress("LongMethod")
@Composable
internal fun FinishScreenComposable(
    onButtonClick: () -> Unit,
    onReloadClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(color = 0xFF000000),
                        Color(color = 0xFF0E1448),
                    )
                )
            )
    )

    Column(
        modifier = modifier
            .padding(vertical = 14.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_checkmark),
                tint = Color.Unspecified,
                modifier = Modifier.size(50.dp),
                contentDescription = null
            )
            Text(
                text = stringResource(Res.string.bwfi_title),
                fontSize = 20.sp,
                color = LocalCorruptedPallet.current.white.onColor,
            )
            Text(
                text = stringResource(Res.string.bwfi_desc),
                fontSize = 12.sp,
                color = Color(color = 0x80FFFFFF) // todo
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BIconButton(
                painter = painterResource(Res.drawable.ic_reload),
                background = Color(color = 0x1AFFFFFF), // todo
                shape = CircleShape,
                onClick = onReloadClick,
                modifier = Modifier.size(34.dp)
            )
            BChipButton(
                modifier = Modifier,
                onClick = onButtonClick,
                text = stringResource(Res.string.bwfi_action),
                painter = null,
                contentColor = LocalCorruptedPallet.current.white.onColor,
                background = Color(color = 0x0DFFFFFF), // todo
                fontSize = 16.sp,
                contentPadding = PaddingValues(
                    vertical = 10.dp,
                    horizontal = 14.dp
                )
            )
        }
    }
}

@Preview
@Composable
private fun PreviewFinishScreenComposable() {
    BusyBarThemeInternal {
        FinishScreenComposable(
            onButtonClick = {},
            onReloadClick = {}
        )
    }
}
