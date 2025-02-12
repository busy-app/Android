package com.flipperdevices.bsb.timer.setup.composable.timer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import busystatusbar.components.bsb.timer.setup.impl.generated.resources.Res
import busystatusbar.components.bsb.timer.setup.impl.generated.resources.ic_block
import busystatusbar.components.bsb.timer.setup.impl.generated.resources.ic_sound_on
import com.flipperdevices.bsb.core.theme.LocalPallet
import com.flipperdevices.ui.cardframe.MediumCardFrameComposable
import org.jetbrains.compose.resources.painterResource

@Composable
fun TimerSoundAppsOptionComposable(
    onSoundClick: () -> Unit,
    onBlockedAppsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        MediumCardFrameComposable(
            title = "Sound",
            desc = "On",
            icon = painterResource(Res.drawable.ic_sound_on),
            modifier = Modifier.weight(1f),
            onClick = onSoundClick,
            iconTint = LocalPallet.current
                .transparent
                .whiteInvert
                .primary
                .copy(alpha = 0.5f)
        )

        MediumCardFrameComposable(
            title = "Blocked apps",
            desc = "14",
            icon = painterResource(Res.drawable.ic_block),
            modifier = Modifier.weight(1f),
            onClick = onBlockedAppsClick,
            iconTint = LocalPallet.current
                .transparent
                .whiteInvert
                .primary
                .copy(alpha = 0.5f)
        )
    }
}