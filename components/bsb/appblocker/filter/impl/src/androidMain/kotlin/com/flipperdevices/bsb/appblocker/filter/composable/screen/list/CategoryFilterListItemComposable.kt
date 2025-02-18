package com.flipperdevices.bsb.appblocker.filter.composable.screen.list

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flipperdevices.bsb.appblocker.filter.model.UIAppCategory
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import com.flipperdevices.bsb.core.theme.LocalPallet
import com.flipperdevices.core.ktx.common.clickableRipple
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun CategoryFilterListItemComposable(
    category: UIAppCategory,
    onClick: (Boolean) -> Unit,
    modifier: Modifier
) {
    Row(
        modifier = modifier
            .padding(vertical = 12.dp)
            .clickableRipple { onClick(!category.isBlocked) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            modifier = Modifier.size(24.dp),
            selected = category.isBlocked,
            onClick = { onClick(!category.isBlocked) },
            colors = RadioButtonDefaults.colors(
                selectedColor = LocalPallet.current.accent.device.primary,
                unselectedColor = LocalPallet.current.transparent.whiteInvert.quaternary
            )
        )

        val title = stringResource(category.categoryEnum.title)

        Icon(
            modifier = Modifier
                .size(32.dp)
                .padding(start = 12.dp, end = 8.dp),
            painter =
            painterResource(category.categoryEnum.icon),
            contentDescription = title
        )

        Text(
            text = title,
            fontSize = 18.sp,
            fontFamily = LocalBusyBarFonts.current.pragmatica,
            fontWeight = FontWeight.W500,
            color = Color(0xFFFFFFFF),
        )
    }
}