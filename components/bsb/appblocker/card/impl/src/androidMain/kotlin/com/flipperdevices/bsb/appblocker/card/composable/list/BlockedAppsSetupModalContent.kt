package com.flipperdevices.bsb.appblocker.card.composable.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import busystatusbar.components.bsb.appblocker.card.impl.generated.resources.Res
import busystatusbar.components.bsb.appblocker.card.impl.generated.resources.ic_block
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalPallet
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
private fun BlockedAppsBoxComposable(
    title: String,
    appIcons: ImmutableList<Painter>,
    onAddApps: () -> Unit,
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
            EmptyListAppsBoxComposable(onClick = onAddApps)
        } else {
            FilledListAppsBoxComposable(
                items = appIcons,
                onClick = onAddApps
            )
        }
    }
}