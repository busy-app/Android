package com.flipperdevices.bsb.appblocker.filter.composable.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import busystatusbar.components.bsb.appblocker.filter.impl.generated.resources.Res
import busystatusbar.components.bsb.appblocker.filter.impl.generated.resources.appblocker_filter_select_all
import busystatusbar.components.bsb.appblocker.filter.impl.generated.resources.appblocker_filter_select_none
import busystatusbar.components.bsb.appblocker.filter.impl.generated.resources.appblocker_filter_title
import com.flipperdevices.bsb.appblocker.filter.model.list.AppBlockerFilterScreenState
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import com.flipperdevices.core.ktx.common.clickableRipple
import org.jetbrains.compose.resources.stringResource

@Composable
fun AppBlockerFilterHeaderComposable(
    screenState: AppBlockerFilterScreenState.Loaded,
    onSelectAll: () -> Unit,
    onDeselectAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(Res.string.appblocker_filter_title),
            fontSize = 24.sp,
            fontFamily = LocalBusyBarFonts.current.pragmatica,
            fontWeight = FontWeight.W600,
            color = LocalCorruptedPallet.current.white.invert,
        )
        val isAllSelected = remember(screenState.categories) {
            screenState.categories.find { it.isBlocked.not() } == null
        }

        Text(
            modifier = Modifier.padding(vertical = 12.dp)
                .clickableRipple {
                    if (isAllSelected) {
                        onDeselectAll()
                    } else {
                        onSelectAll()
                    }
                },
            text = stringResource(
                if (isAllSelected) {
                    Res.string.appblocker_filter_select_none
                } else {
                    Res.string.appblocker_filter_select_all
                }
            ),
            fontSize = 18.sp,
            fontFamily = LocalBusyBarFonts.current.pragmatica,
            fontWeight = FontWeight.W500,
            color = LocalCorruptedPallet.current.transparent.whiteInvert.primary,
        )
    }
}
