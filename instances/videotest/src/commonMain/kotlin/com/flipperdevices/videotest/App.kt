package com.flipperdevices.videotest

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import busystatusbar.instances.videotest.generated.resources.Res
import busystatusbar.instances.videotest.generated.resources.sample_fire_frame
import com.flipperdevices.bsb.core.theme.BusyBarTheme
import com.flipperdevices.ui.video.BSBVideoPlayer
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
@Suppress("UnusedParameter")
fun App(
    modifier: Modifier = Modifier
) {
    BusyBarTheme(darkMode = true) {
        Box(
            modifier.safeDrawingPadding()
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            BSBVideoPlayer(
                modifier = Modifier.fillMaxWidth(),
                uri = Res.getUri("files/sample_fire.mp4"),
                firstFrame = Res.drawable.sample_fire_frame
            )
        }
    }
}
