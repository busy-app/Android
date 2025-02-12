package com.flipperdevices.bsb.timer.setup.composable.blockedapps

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalPallet
import com.flipperdevices.ui.button.dashedBorder
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun EmptyListAppsBoxComposable(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth()
            .dashedBorder(
                color = LocalPallet.current.transparent.whiteInvert.quaternary,
                radius = 12.dp
            )
            .clip(RoundedCornerShape(12.dp))
            .background(LocalPallet.current.transparent.whiteInvert.quinary)
            .clickable(onClick = onClick)
            .padding(vertical = 28.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "+ Add apps",
            color = LocalPallet.current
                .transparent
                .whiteInvert
                .primary,
            fontSize = 18.sp
        )
    }
}

@Composable
@Preview
private fun EmptyListAppsBoxComposablePreview() {
    BusyBarThemeInternal {
        EmptyListAppsBoxComposable(
            onClick = {}
        )
    }
}
