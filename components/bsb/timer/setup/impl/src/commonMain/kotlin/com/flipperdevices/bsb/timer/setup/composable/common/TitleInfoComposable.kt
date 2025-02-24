package com.flipperdevices.bsb.timer.setup.composable.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import busystatusbar.components.bsb.timer.setup.impl.generated.resources.Res
import busystatusbar.components.bsb.timer.setup.impl.generated.resources.ic_work
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun TitleInfoComposable(
    icon: Painter,
    title: String,
    desc: String?,
    modifier: Modifier = Modifier.Companion,
    iconTint: Color = Color.Companion.Unspecified,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Companion.CenterVertically
        ) {
            Icon(
                painter = icon,
                tint = iconTint,
                contentDescription = null,
                modifier = Modifier.Companion.size(32.dp)
            )
            Text(
                text = title,
                color = LocalCorruptedPallet.current
                    .white
                    .invert,
                fontSize = 24.sp
            )
        }
        desc?.let {
            Text(
                text = desc,
                color = LocalCorruptedPallet.current
                    .neutral
                    .tertiary,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
@Preview
private fun TitleInfoComposablePreview() {
    BusyBarThemeInternal {
        TitleInfoComposable(
            modifier = Modifier.padding(horizontal = 16.dp),
            title = "Work",
            desc = "Pick how long you want to work during each interval",
            icon = painterResource(Res.drawable.ic_work)
        )
    }
}
