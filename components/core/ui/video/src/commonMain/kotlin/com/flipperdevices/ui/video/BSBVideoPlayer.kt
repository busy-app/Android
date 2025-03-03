package com.flipperdevices.ui.video

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.DrawableResource

@Composable
expect fun BSBVideoPlayer(
    uri: String,
    firstFrame: DrawableResource,
    modifier: Modifier = Modifier,
    fallback: DrawableResource = firstFrame,
)
