package com.flipperdevices.bsb.timer.setup.composable.intervals

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import busystatusbar.components.bsb.timer.setup.impl.generated.resources.Res
import busystatusbar.components.bsb.timer.setup.impl.generated.resources.ts_bs_sound_desc
import busystatusbar.components.bsb.timer.setup.impl.generated.resources.ts_bs_sound_title
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.ui.options.OptionSwitch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SoundSetupModalBottomSheetContent(
    isChecked: Boolean,
    onChange: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OptionSwitch(
        modifier = modifier,
        text = stringResource(Res.string.ts_bs_sound_title),
        infoText = stringResource(Res.string.ts_bs_sound_desc),
        checked = isChecked,
        verticalAlignment = Alignment.Top,
        onCheckChange = { onChange.invoke() }
    )
}

@Suppress("MagicNumber")
@Composable
@Preview
private fun SoundSetupModalBottomSheetContentPreview() {
    BusyBarThemeInternal {
        SoundSetupModalBottomSheetContent(
            isChecked = true,
            onChange = {}
        )
    }
}
