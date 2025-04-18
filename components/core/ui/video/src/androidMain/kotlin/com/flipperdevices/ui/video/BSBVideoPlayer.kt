package com.flipperdevices.ui.video

import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
    val exoPlayerState = rememberPlayer(uri)

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        PlayerSurface(
            modifier = Modifier.resizeWithContentScale(
                contentScale = ContentScale.FillWidth,
                sourceSizeDp = null
            ),
            player = exoPlayerState.exoPlayer,
            surfaceType = SURFACE_TYPE_TEXTURE_VIEW,
        )

        if (!exoPlayerState.isReady) {
            Image(
                modifier = Modifier,
                painter = painterResource(firstFrame),
                contentDescription = null
            )
        }
    }
}

interface ExoPlayerState {
    val exoPlayer: ExoPlayer
    val isReady: Boolean
}

class ExoPlayerStateImpl(override val exoPlayer: ExoPlayer) : ExoPlayerState {
    val isReadyState = mutableStateOf(false)
    override val isReady: Boolean
        get() = isReadyState.value
}

@OptIn(UnstableApi::class)
@Composable
fun rememberPlayer(uri: String): ExoPlayerState {
    val context = LocalContext.current
    val exoPlayerState = remember {
        ExoPlayerStateImpl(
            exoPlayer = ExoPlayer.Builder(context)
                .setMediaSourceFactory(
                    ProgressiveMediaSource.Factory(DefaultDataSource.Factory(context))
                )
                .setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING)
                .build()
                .apply {
                    playWhenReady = true
                    repeatMode = Player.REPEAT_MODE_ALL
                }
        )
    }
    DisposableEffect(exoPlayerState.exoPlayer, uri) {
        val listener = object : Player.Listener {
            override fun onRenderedFirstFrame() {
                exoPlayerState.isReadyState.value = true
            }
        }
        exoPlayerState.exoPlayer.addListener(listener)
        exoPlayerState.exoPlayer.addMediaItem(MediaItem.fromUri(uri))
        exoPlayerState.exoPlayer.prepare()
        onDispose {
            exoPlayerState.exoPlayer.removeListener(listener)
            exoPlayerState.exoPlayer.release()
        }
    }
    return exoPlayerState
}
