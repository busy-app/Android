package com.flipperdevices.ui.video

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.sanghun.compose.video.VideoPlayer
import io.sanghun.compose.video.uri.VideoPlayerMediaItem

@Composable
fun VideoComposable(
    uri: String,
    modifier: Modifier = Modifier
) {
    VideoPlayer(
        mediaItems = listOf(
            VideoPlayerMediaItem.StorageMediaItem(
                storageUri = Uri.parse(uri)
            )
        )
    )
}