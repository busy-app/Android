package com.flipperdevices.bsb.timer.setup.composable.blockedapps

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import busystatusbar.components.bsb.timer.setup.impl.generated.resources.Res
import busystatusbar.components.bsb.timer.setup.impl.generated.resources.ic_arrow_right
import busystatusbar.components.bsb.timer.setup.impl.generated.resources.ic_block
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalPallet
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun FilledListAppsBoxComposable(
    items: ImmutableList<Painter>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lazyListState = rememberLazyListState()
    Box(
        modifier = modifier.fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
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
                painter = painterResource(Res.drawable.ic_arrow_right), // todo
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
@Preview
private fun FilledListAppsBoxComposablePreview() {
    BusyBarThemeInternal {
        FilledListAppsBoxComposable(
            items = List(size = 24) { painterResource(Res.drawable.ic_block) }.toImmutableList(),
            onClick = {}
        )
    }
}
