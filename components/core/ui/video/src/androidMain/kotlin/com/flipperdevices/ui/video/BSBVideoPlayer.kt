package com.flipperdevices.ui.video

import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.compose.runtime.remember
import androidx.compose.ui.layout.ContentScale
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.compose.PlayerSurface
import androidx.media3.ui.compose.SURFACE_TYPE_TEXTURE_VIEW
import androidx.media3.ui.compose.modifiers.resizeWithContentScale
import org.jetbrains.compose.resources.DrawableResource

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

    LaunchedEffect(exoPlayer, uri) {
        exoPlayer.addMediaItem(MediaItem.fromUri(uri))
        exoPlayer.prepare()
    }

    PlayerSurface(
        modifier = modifier.resizeWithContentScale(
            contentScale = ContentScale.FillWidth,
            sourceSizeDp = null
        ),
        player = exoPlayer,
        surfaceType = SURFACE_TYPE_TEXTURE_VIEW,
    )
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
