package com.flipperdevices.bsb.timer.common.composable.appbar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import busystatusbar.components.bsb.timer.common.generated.resources.Res
import busystatusbar.components.bsb.timer.common.generated.resources.ic_back
import busystatusbar.components.bsb.timer.common.generated.resources.ic_three_dots
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import com.flipperdevices.bsb.root.api.LocalRootNavigation
import com.flipperdevices.bsb.root.model.RootNavigationConfig
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TimerAppBarComposable(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxWidth().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.align(Alignment.CenterStart),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_back),
                tint = LocalCorruptedPallet.current
                    .transparent
                    .whiteInvert
                    .secondary,
                contentDescription = null,
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onBackClick)
            )
        }

        Box(
            modifier = Modifier.align(Alignment.TopEnd),
            contentAlignment = Alignment.Center
        ) {
            val rootNavigation = LocalRootNavigation.current
            Icon(
                painter = painterResource(Res.drawable.ic_three_dots),
                tint = LocalCorruptedPallet.current
                    .transparent
                    .whiteInvert
                    .secondary,
                contentDescription = null,
                modifier = Modifier.size(44.dp)
                    .clickable {
                        rootNavigation.push(RootNavigationConfig.Profile(null))
                    }
            )
        }
    }
}

@Composable
@Preview
private fun TimerAppBarComposablePreview() {
    BusyBarThemeInternal {
        TimerAppBarComposable(onBackClick = {})
    }
}
