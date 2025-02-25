package com.flipperdevices.bsb.appblocker.filter.composable.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import busystatusbar.components.bsb.appblocker.filter.impl.generated.resources.Res
import busystatusbar.components.bsb.appblocker.filter.impl.generated.resources.appblocker_filter_btn_save
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import com.flipperdevices.ui.button.BChipButton
import org.jetbrains.compose.resources.stringResource

@Composable
fun AppBlockerSaveButtonComposable(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BChipButton(
        modifier = modifier,
        onClick = onClick,
        text = stringResource(Res.string.appblocker_filter_btn_save),
        painter = null,
        contentColor = LocalCorruptedPallet.current
            .white
            .invert,
        background = LocalCorruptedPallet.current
            .neutral
            .primary,
        contentPadding = PaddingValues(
            horizontal = 32.dp,
            vertical = 24.dp
        )
    )
}

@Composable
@Preview
private fun AppBlockerSaveButtonComposablePreview() {
    BusyBarThemeInternal {
        AppBlockerSaveButtonComposable(onClick = {})
    }
}
