package com.flipperdevices.ui.options

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ExpandableOptionComposable(
    isExpanded: Boolean,
    header: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .animateContentSize( // Animation
                animationSpec = tween(
                    durationMillis = 70, // Animation Speed
                    easing = LinearOutSlowInEasing // Animation Type
                )
            ),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            header.invoke(this)

            AnimatedVisibility(
                visible = isExpanded,
                exit = fadeOut(
                    tween(
                        durationMillis = 100,
                        easing = LinearOutSlowInEasing
                    )
                ) + shrinkVertically(
                    tween(
                        durationMillis = 200,
                        easing = LinearOutSlowInEasing
                    )
                )
            ) {
                content.invoke()
            }
        }
    }
}

@Preview
@Composable
private fun ExpandableOptionComposablePreview() {
    var isExpanded by remember { mutableStateOf(false) }
    BusyBarThemeInternal {
        ExpandableOptionComposable(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Red)
                .clickable { isExpanded = !isExpanded },
            isExpanded = isExpanded,
            content = {
                Column {
                    repeat(times = 4) {
                        OptionHref(
                            icon = rememberVectorPainter(Icons.Default.Call),
                            text = TEXT,
                            onClick = {},
                        )
                    }
                }
            },
            header = {
                Text("I'm cool header!")
            }
        )
    }
}
