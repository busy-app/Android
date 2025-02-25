package com.flipperdevices.bsb.appblocker.filter.composable.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import busystatusbar.components.bsb.appblocker.filter.impl.generated.resources.Res
import busystatusbar.components.bsb.appblocker.filter.impl.generated.resources.appblocker_card_all
import busystatusbar.components.bsb.appblocker.filter.impl.generated.resources.ic_app_type_other
import busystatusbar.components.bsb.appblocker.filter.impl.generated.resources.ic_arrow_right
import busystatusbar.components.bsb.appblocker.filter.impl.generated.resources.ic_more
import com.flipperdevices.bsb.appblocker.filter.model.card.AppIcon
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
@Suppress("LongMethod")
internal fun FilledListAppsBoxComposable(
    isAllBlocked: Boolean,
    items: ImmutableList<AppIcon>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .background(LocalCorruptedPallet.current.transparent.whiteInvert.quinary)
            .padding(14.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            var isAllIconShown by remember { mutableStateOf(false) }
            ReversedRow(
                Modifier.weight(1f),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!isAllIconShown) {
                    Icon(
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .size(24.dp),
                        painter = painterResource(Res.drawable.ic_more),
                        contentDescription = null,
                        tint = Color(color = 0xFF888888)
                    )
                }
                BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                    // Calculated amount of items which will be fitted
                    // Considering every item ~40.dp size
                    val slicedItems = remember(maxWidth, items) {
                        val maxItems = (maxWidth / 40.dp).toInt() + 1
                        when {
                            items.isEmpty() -> items
                            else -> items.subList(0, maxItems.coerceAtMost(items.size))
                        }
                    }
                    PlaceableDetectableRow(
                        modifier = Modifier,
                        onPlacementComplete = { count ->
                            isAllIconShown = count >= items.size
                        }
                    ) {
                        slicedItems.forEach { icon ->
                            AppIconComposable(
                                modifier = Modifier
                                    .padding(start = 4.dp)
                                    .size(40.dp),
                                appIcon = icon
                            )
                        }
                    }
                }
            }

            val blockedAppsCount = if (isAllBlocked) {
                stringResource(Res.string.appblocker_card_all)
            } else if (isAllIconShown) {
                null
            } else {
                "${items.size}"
            }

            if (blockedAppsCount != null) {
                Text(
                    text = blockedAppsCount,
                    color = LocalCorruptedPallet.current
                        .transparent
                        .whiteInvert
                        .primary,
                    fontSize = 18.sp
                )
            }
            Icon(
                painter = painterResource(Res.drawable.ic_arrow_right),
                contentDescription = null,
                tint = LocalCorruptedPallet.current
                    .transparent
                    .whiteInvert
                    .secondary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun ReversedRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    content: @Composable RowScope.() -> Unit
) {
    CompositionLocalProvider(value = LocalLayoutDirection provides LayoutDirection.Rtl) {
        Row(
            modifier = modifier,
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = verticalAlignment,
        ) {
            CompositionLocalProvider(value = LocalLayoutDirection provides LayoutDirection.Ltr) {
                content()
            }
        }
    }
}

@Composable
private fun AppIconComposable(
    appIcon: AppIcon,
    modifier: Modifier = Modifier
) {
    when (appIcon) {
        is AppIcon.Category -> Icon(
            modifier = modifier,
            painter = painterResource(appIcon.icon),
            contentDescription = null,
            tint = Color(color = 0xFFFFFFFF)
        )

        is AppIcon.App -> {
            val context = LocalContext.current
            val drawable = remember(appIcon, context) {
                runCatching {
                    context.packageManager.getApplicationIcon(appIcon.packageName)
                }.getOrNull()
            }
            if (drawable == null) {
                return
            }
            Image(
                modifier = modifier,
                painter = rememberDrawablePainter(drawable),
                contentDescription = null
            )
        }
    }
}

@Composable
@Preview
private fun FilledListAppsBoxComposablePreview() {
    BusyBarThemeInternal {
        Column(
            modifier = Modifier
                .safeDrawingPadding()
                .background(
                    Color(color = 0xFF212121)
                )
        ) {
            FilledListAppsBoxComposable(
                items = List(size = 24) { AppIcon.Category(Res.drawable.ic_app_type_other) }.toImmutableList(),
                onClick = {},
                isAllBlocked = false
            )
            FilledListAppsBoxComposable(
                items = List(size = 2) { AppIcon.Category(Res.drawable.ic_app_type_other) }.toImmutableList(),
                onClick = {},
                isAllBlocked = true
            )
        }
    }
}
