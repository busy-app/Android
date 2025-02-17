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
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalPallet
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
                StatusType.BUSY -> "BUSY"
                StatusType.REST -> "REST"
                StatusType.LONG_REST -> "LONG REST"
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
