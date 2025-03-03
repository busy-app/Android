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
    modifier: Modifier,
    fallback: DrawableResource,
) {
    Image(
        modifier = modifier,
        painter = painterResource(fallback),
        contentDescription = null
    )
}
