package com.flipperdevices.ui.video

import android.content.Context
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.compose.PlayerSurface
import androidx.media3.ui.compose.SURFACE_TYPE_TEXTURE_VIEW
import androidx.media3.ui.compose.modifiers.resizeWithContentScale
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@OptIn(UnstableApi::class)
@Composable
actual fun BSBVideoPlayer(
    uri: String,
    firstFrame: DrawableResource,
    modifier: Modifier,
    fallback: DrawableResource,
) {
    val context = LocalContext.current
    val exoPlayer = rememberPlayer(context)
    var isPlayerReady by remember { mutableStateOf(false) }

    LaunchedEffect(exoPlayer, uri) {
        exoPlayer.addListener(object : Player.Listener {
            override fun onRenderedFirstFrame() {
                isPlayerReady = true
            }
        })
        exoPlayer.addMediaItem(MediaItem.fromUri(uri))
        exoPlayer.prepare()
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        PlayerSurface(
            modifier = Modifier.resizeWithContentScale(
                contentScale = ContentScale.FillWidth,
                sourceSizeDp = null
            ),
            player = exoPlayer,
            surfaceType = SURFACE_TYPE_TEXTURE_VIEW,
        )

        if (!isPlayerReady) {
            Image(
                modifier = Modifier,
                painter = painterResource(firstFrame),
                contentDescription = null
            )
        }
    }
}

@OptIn(UnstableApi::class)
@Composable
fun rememberPlayer(context: Context) = remember {
    ExoPlayer.Builder(context)
        .setMediaSourceFactory(
            ProgressiveMediaSource.Factory(DefaultDataSource.Factory(context))
        )
        .setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING)
        .build()
        .apply {
            playWhenReady = true
            repeatMode = Player.REPEAT_MODE_ALL
        }
}
