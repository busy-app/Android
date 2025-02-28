package com.flipperdevices.bsbwearable.active.composable

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipColors
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet

@Composable
fun BWChipButton(
    onClick: () -> Unit,
    label: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ChipColors = ChipDefaults.chipColors(
        backgroundColor = LocalCorruptedPallet.current.transparent.whiteInvert.tertiary.copy(alpha = 0.1f),
//        contentColor = MaterialTheme.colors.onPrimary,
//        secondaryContentColor = MaterialTheme.colors.secondary,
//        iconColor = MaterialTheme.colors.onPrimary,
//        disabledBackgroundColor = MaterialTheme.colors.primary.copy(0.5f),
//        disabledContentColor = MaterialTheme.colors.onPrimary.copy(0.5f),
//        disabledSecondaryContentColor = MaterialTheme.colors.secondary.copy(0.5f),
//        disabledIconColor = MaterialTheme.colors.onPrimary.copy(0.5f)
    ),
    icon: (@Composable BoxScope.() -> Unit)? = null
) {
    Chip(
        modifier = modifier,
        label = label,
        onClick = onClick,
        icon = icon,
        colors = colors,
        enabled = enabled
    )
}

@Composable
fun BWChipButton(
    text: String?,
    painter: Painter?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ChipColors = ChipDefaults.chipColors(
        backgroundColor = LocalCorruptedPallet.current.transparent.whiteInvert.tertiary.copy(alpha = 0.1f),
//        contentColor = MaterialTheme.colors.onPrimary,
//        secondaryContentColor = MaterialTheme.colors.secondary,
//        iconColor = MaterialTheme.colors.onPrimary,
//        disabledBackgroundColor = MaterialTheme.colors.primary.copy(0.5f),
//        disabledContentColor = MaterialTheme.colors.onPrimary.copy(0.5f),
//        disabledSecondaryContentColor = MaterialTheme.colors.secondary.copy(0.5f),
//        disabledIconColor = MaterialTheme.colors.onPrimary.copy(0.5f)
    ),
) {
    BWChipButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = colors,
        label = {
            text?.let {
                Text(
                    text = text,
                    color = Color(color = 0x80FFFFFF), // todo
                    fontSize = 16.sp
                )
            }
        },
        icon = {
            painter?.let {
                Icon(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .size(16.dp)
                        .wrapContentSize(align = Alignment.Center),
                    tint = colors.iconColor(enabled = enabled).value
                )
            }
        }
    )
}
