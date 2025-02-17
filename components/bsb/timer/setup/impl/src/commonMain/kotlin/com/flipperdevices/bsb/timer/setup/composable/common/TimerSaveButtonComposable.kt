package com.flipperdevices.bsb.timer.setup.composable.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import busystatusbar.components.bsb.timer.setup.impl.generated.resources.Res
import busystatusbar.components.bsb.timer.setup.impl.generated.resources.ts_bs_save
import com.flipperdevices.ui.button.BChipButton
import org.jetbrains.compose.resources.stringResource

@Composable
fun TimerSaveButtonComposable(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        BChipButton(
            text = stringResource(Res.string.ts_bs_save),
            painter = null,
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(fraction = 0.4f)
        )
    }
}
