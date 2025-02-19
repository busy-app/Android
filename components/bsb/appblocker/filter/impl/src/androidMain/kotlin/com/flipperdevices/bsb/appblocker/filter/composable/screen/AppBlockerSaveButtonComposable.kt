package com.flipperdevices.bsb.appblocker.filter.composable.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import busystatusbar.components.bsb.appblocker.filter.impl.generated.resources.Res
import busystatusbar.components.bsb.appblocker.filter.impl.generated.resources.appblocker_filter_btn_save
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import com.flipperdevices.bsb.core.theme.LocalPallet
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
        contentColor = LocalPallet.current
            .white
            .invert,
        background = LocalPallet.current
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