package com.flipperdevices.bsb.timer.common.composable.appbar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import busystatusbar.components.bsb.timer.common.generated.resources.Res
import busystatusbar.components.bsb.timer.common.generated.resources.tc_status_busy
import busystatusbar.components.bsb.timer.common.generated.resources.tc_status_long_rest
import busystatusbar.components.bsb.timer.common.generated.resources.tc_status_rest
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalPallet
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun StatusLowBarComposable(
    type: StatusType,
    modifier: Modifier = Modifier,
    statusDesc: String? = null,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = when (type) {
                StatusType.BUSY -> stringResource(Res.string.tc_status_busy)
                StatusType.REST -> stringResource(Res.string.tc_status_rest)
                StatusType.LONG_REST -> stringResource(Res.string.tc_status_long_rest)
            },
            color = LocalPallet.current
                .white
                .invert,
            fontSize = 40.sp
        )

        statusDesc?.let {
            Text(
                text = statusDesc,
                color = LocalPallet.current
                    .transparent
                    .whiteInvert
                    .secondary,
                fontSize = 17.sp
            )
        }
    }
}

enum class StatusType {
    BUSY, REST, LONG_REST
}

@Preview
@Composable
private fun PreviewStatusLowBarComposable() {
    BusyBarThemeInternal {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            StatusType.entries.forEach { entry ->
                StatusLowBarComposable(
                    type = entry,
                    statusDesc = "2/3"
                )
            }
        }
    }
}
