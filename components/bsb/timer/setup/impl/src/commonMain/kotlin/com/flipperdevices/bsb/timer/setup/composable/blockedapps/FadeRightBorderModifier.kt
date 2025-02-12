package com.flipperdevices.bsb.timer.setup.composable.blockedapps

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer

// Here's modifier with composable. It will anyway recompose because we need lazyListState
@Composable
@Suppress("MagicNumber")
internal fun Modifier.fadeRightBorder(lazyListState: LazyListState): Modifier {
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
