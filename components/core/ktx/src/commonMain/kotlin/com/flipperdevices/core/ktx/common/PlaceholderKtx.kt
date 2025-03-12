package com.flipperdevices.core.ktx.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import com.eygraber.compose.placeholder.PlaceholderHighlight
import com.eygraber.compose.placeholder.placeholder
import com.eygraber.compose.placeholder.shimmer

private val placeholderColor = Color(color = 0x2A2A2A)

@Composable
@Suppress("ModifierComposable")
fun Modifier.placeholder(
    visible: Boolean,
    shape: Shape = RectangleShape,
    highlightColor: Color = placeholderColor,
    color: Color = placeholderColor.copy(alpha = 0.2f),
) = placeholder(
    visible = visible,
    color = color,
    shape = shape,
    highlight = PlaceholderHighlight.shimmer(
        highlightColor = highlightColor
    )
)
