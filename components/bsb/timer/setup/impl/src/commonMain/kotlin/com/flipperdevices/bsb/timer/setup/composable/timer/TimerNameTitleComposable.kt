package com.flipperdevices.bsb.timer.setup.composable.timer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import busystatusbar.components.bsb.timer.setup.impl.generated.resources.Res
import busystatusbar.components.bsb.timer.setup.impl.generated.resources.ts_bs_timer_name_title
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun TimerNameTitleComposable(
    name: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(
                LocalCorruptedPallet.current.transparent
                    .blackInvert
                    .secondary
                    .copy(alpha = 0.2f)
            )
            .padding(vertical = 8.dp)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(Res.string.ts_bs_timer_name_title),
            fontSize = 14.sp,
            color = LocalCorruptedPallet.current
                .transparent
                .whiteInvert
                .secondary
                .copy(alpha = 0.3f),
        )
        Text(
            text = name,
            fontSize = 40.sp,
            color = LocalCorruptedPallet.current
                .white
                .invert,
        )
    }
}
