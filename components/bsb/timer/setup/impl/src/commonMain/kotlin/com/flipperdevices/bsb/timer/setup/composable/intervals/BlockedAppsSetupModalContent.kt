package com.flipperdevices.bsb.timer.setup.composable.intervals

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import busystatusbar.components.bsb.timer.setup.impl.generated.resources.Res
import busystatusbar.components.bsb.timer.setup.impl.generated.resources.ic_block
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalPallet
import com.flipperdevices.bsb.preference.model.TimerSettings
import com.flipperdevices.bsb.timer.setup.composable.common.TimerSaveButtonComposable
import com.flipperdevices.bsb.timer.setup.composable.common.TitleInfoComposable
import com.flipperdevices.ui.button.dashedBorder
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
private fun EmptyListAppsBoxComposable(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth()
            .dashedBorder(
                color = LocalPallet.current.transparent.whiteInvert.quaternary,
                radius = 12.dp
            )
            .clip(RoundedCornerShape(12.dp))
            .background(LocalPallet.current.transparent.whiteInvert.quinary)
            .padding(vertical = 28.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "+ Add apps",
            color = LocalPallet.current
                .transparent
                .whiteInvert
                .primary,
            fontSize = 18.sp
        )
    }
}


@Composable
fun Modifier.fadeRightBorder(lazyListState: LazyListState): Modifier {
    val startAlpha by animateFloatAsState(
        targetValue = when {
            lazyListState.canScrollBackward -> 1f
            else -> 0f
        }
    )
    val endAlpha by animateFloatAsState(
        targetValue = when {
            lazyListState.canScrollForward -> 1f
            else -> 0f
        }
    )
    return this.then(
        Modifier
            .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
            .drawWithContent {
                drawContent()
                drawRoundRect(
                    brush = Brush.horizontalGradient(
                        0.05f to Color.White.copy(alpha = startAlpha),
                        0.4f to Color.Transparent,
                        1f to Color.Transparent,
                    ),
                    blendMode = BlendMode.DstOut
                )
                drawRoundRect(
                    brush = Brush.horizontalGradient(
                        0f to Color.Transparent,
                        0.6f to Color.Transparent,
                        0.95f to Color.White.copy(alpha = endAlpha),
                    ),
                    blendMode = BlendMode.DstOut
                )
            }
    )
}

@Composable
private fun FilledListAppsBoxComposable(items: List<Painter>, modifier: Modifier = Modifier) {
    val lazyListState = rememberLazyListState()
    Box(
        modifier = modifier.fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(LocalPallet.current.transparent.whiteInvert.quinary)
            .padding(14.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            LazyRow(
                state = lazyListState,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.weight(1f).fadeRightBorder(lazyListState)
            ) {
                items(items) { painter ->
                    Icon(
                        painter = painter,
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(44.dp)
                    )
                }
            }
            Text(
                text = "${items.size}",
                color = LocalPallet.current
                    .transparent
                    .whiteInvert
                    .primary,
                fontSize = 18.sp
            )
            Icon(
                imageVector = Icons.Filled.KeyboardArrowRight, // todo
                contentDescription = null,
                tint = LocalPallet.current
                    .transparent
                    .whiteInvert
                    .secondary,
                modifier = Modifier.size(24.dp)
            )
        }

    }
}

@Composable
private fun BlockedAppsBoxComposable(
    title: String,
    appIcons: List<Painter>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = title,
            color = LocalPallet.current
                .transparent
                .whiteInvert
                .primary,
            fontSize = 18.sp
        )
        if (appIcons.isEmpty()) {
            EmptyListAppsBoxComposable()
        } else {
            FilledListAppsBoxComposable(appIcons)
        }
    }
}

@Composable
fun BlockedAppsSetupModalBottomSheetContent(
    blockedAppsDuringRest: List<Painter>,
    blockedAppsDuringWork: List<Painter>,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        horizontalAlignment = Alignment.Start,
        modifier = modifier.fillMaxWidth().navigationBarsPadding()
    ) {
        TitleInfoComposable(
            modifier = Modifier.padding(horizontal = 16.dp),
            title = "Blocked apps",
            desc = null,
            icon = painterResource(Res.drawable.ic_block)
        )

        BlockedAppsBoxComposable(
            modifier = Modifier.padding(horizontal = 16.dp),
            title = "Blocked apps during work interval:",
            appIcons = blockedAppsDuringWork
        )

        BlockedAppsBoxComposable(
            modifier = Modifier.padding(horizontal = 16.dp),
            title = "Blocked apps during rest interval:",
            appIcons = blockedAppsDuringRest
        )

        TimerSaveButtonComposable(onClick = onSaveClick)
        Spacer(Modifier.height(16.dp))
    }
}

@Suppress("MagicNumber")
@Composable
@Preview
private fun BlockedAppsSetupModalBottomSheetContentPreview() {
    BusyBarThemeInternal {
        BlockedAppsSetupModalBottomSheetContent(
            onSaveClick = {},
            blockedAppsDuringRest = List(24) { painterResource(Res.drawable.ic_block) },
            blockedAppsDuringWork = emptyList()

        )
    }
}
