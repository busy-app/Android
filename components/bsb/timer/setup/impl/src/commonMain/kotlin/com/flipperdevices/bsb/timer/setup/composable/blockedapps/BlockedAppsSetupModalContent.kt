package com.flipperdevices.bsb.timer.setup.composable.blockedapps

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
import busystatusbar.components.bsb.timer.setup.impl.generated.resources.Res
import busystatusbar.components.bsb.timer.setup.impl.generated.resources.ic_block
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalPallet
import com.flipperdevices.bsb.timer.setup.composable.common.TimerSaveButtonComposable
import com.flipperdevices.bsb.timer.setup.composable.common.TitleInfoComposable
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

@Composable
fun BlockedAppsSetupModalBottomSheetContent(
    blockedAppsDuringRest: ImmutableList<Painter>,
    blockedAppsDuringWork: ImmutableList<Painter>,
    onAddBlockedAppsDuringWorkClick: () -> Unit,
    onAddBlockedAppsDuringRestClick: () -> Unit,
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
            appIcons = blockedAppsDuringWork,
            onAddApps = onAddBlockedAppsDuringWorkClick
        )

        BlockedAppsBoxComposable(
            modifier = Modifier.padding(horizontal = 16.dp),
            title = "Blocked apps during rest interval:",
            appIcons = blockedAppsDuringRest,
            onAddApps = onAddBlockedAppsDuringRestClick
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
            blockedAppsDuringRest = List(24) { painterResource(Res.drawable.ic_block) }.toImmutableList(),
            blockedAppsDuringWork = persistentListOf(),
            onAddBlockedAppsDuringRestClick = {},
            onAddBlockedAppsDuringWorkClick = {}
        )
    }
}
