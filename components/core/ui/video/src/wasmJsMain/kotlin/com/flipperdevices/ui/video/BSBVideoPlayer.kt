package com.flipperdevices.ui.video

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
actual fun BSBVideoPlayer(
    uri: String,
    firstFrame: DrawableResource,
    fallback: DrawableResource,
    modifier: Modifier
) {
    Image(
        modifier = modifier,
        painter = painterResource(fallback),
        contentDescription = null
    )
}