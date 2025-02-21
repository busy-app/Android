package com.flipperdevices.bsb.appblocker.filter.composable.screen.list

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import busystatusbar.components.bsb.appblocker.filter.impl.generated.resources.Res
import busystatusbar.components.bsb.appblocker.filter.impl.generated.resources.appblocker_card_all
import busystatusbar.components.bsb.appblocker.filter.impl.generated.resources.ic_arrow
import com.flipperdevices.bsb.appblocker.filter.model.list.UIAppCategory
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import com.flipperdevices.bsb.core.theme.LocalPallet
import com.flipperdevices.core.ktx.common.clickableRipple
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private const val FULLY_ROTATED_ANGEL = 180f

@Composable
@Suppress("LongMethod")
fun CategoryFilterListItemComposable(
    category: UIAppCategory,
    onSwitch: (Boolean) -> Unit,
    onClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickableRipple { onClick(!category.isHidden) }
            .padding(vertical = 12.dp)
            .padding(top = 12.dp, bottom = 12.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            modifier = Modifier
                .padding(start = 16.dp)
                .size(24.dp),
            selected = category.isBlocked,
            onClick = { onSwitch(!category.isBlocked) },
            colors = RadioButtonDefaults.colors(
                selectedColor = LocalPallet.current.accent.device.primary,
                unselectedColor = LocalPallet.current.transparent.whiteInvert.quaternary
            )
        )

        val title = stringResource(category.categoryEnum.title)

        Icon(
            modifier = Modifier
                .padding(start = 12.dp, end = 8.dp)
                .size(32.dp),
            painter = painterResource(category.categoryEnum.icon),
            contentDescription = title,
            tint = Color(color = 0xFFFFFFFF)
        )

        Text(
            modifier = Modifier.weight(1f),
            text = title,
            fontSize = 18.sp,
            fontFamily = LocalBusyBarFonts.current.pragmatica,
            fontWeight = FontWeight.W500,
            color = Color(color = 0xFFFFFFFF),
        )

        val blockedAppsText = if (category.isBlocked) {
            stringResource(Res.string.appblocker_card_all)
        } else {
            val blockedAppsCount = remember(category.apps) {
                category.apps.count { it.isBlocked }
            }
            if (blockedAppsCount > 0) {
                blockedAppsCount.toString()
            } else {
                null
            }
        }

        if (blockedAppsText != null) {
            Text(
                modifier = Modifier.padding(start = 12.dp),
                text = blockedAppsText,
                fontSize = 14.sp,
                lineHeight = 14.sp,
                fontFamily = LocalBusyBarFonts.current.pragmatica,
                fontWeight = FontWeight.W500,
                color = LocalPallet.current.transparent.whiteInvert.secondary,
            )
        }

        if (category.apps.isNotEmpty()) {
            val rotateAngel = if (category.isHidden) {
                FULLY_ROTATED_ANGEL
            } else {
                0f
            }
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .rotate(rotateAngel),
                painter = painterResource(Res.drawable.ic_arrow),
                contentDescription = null,
                tint = LocalPallet.current.transparent.whiteInvert.secondary
            )
        }
    }
}
