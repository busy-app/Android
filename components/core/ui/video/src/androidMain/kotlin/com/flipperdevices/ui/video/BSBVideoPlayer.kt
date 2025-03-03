package com.flipperdevices.ui.video

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.sanghun.compose.video.RepeatMode
import io.sanghun.compose.video.VideoPlayer
import io.sanghun.compose.video.uri.VideoPlayerMediaItem
import org.jetbrains.compose.resources.DrawableResource

@Composable
actual fun BSBVideoPlayer(
    uri: String,
    firstFrame: DrawableResource,
    modifier: Modifier,
    fallback: DrawableResource,
) {
    VideoPlayer(
        modifier = modifier,
        mediaItems = listOf(
            VideoPlayerMediaItem.StorageMediaItem(
                storageUri = Uri.parse(uri)
            )
        ),
        usePlayerController = false,
        handleAudioFocus = false,
        enablePip = false,
        volume = 0f,
        repeatMode = RepeatMode.ALL
    )
}
