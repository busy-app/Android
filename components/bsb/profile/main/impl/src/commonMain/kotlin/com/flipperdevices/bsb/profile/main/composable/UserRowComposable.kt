package com.flipperdevices.bsb.profile.main.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import busystatusbar.components.bsb.profile.main.impl.generated.resources.Res
import busystatusbar.components.bsb.profile.main.impl.generated.resources.ic_user
import com.flipperdevices.bsb.cloud.model.BSBUser
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import com.flipperdevices.bsb.core.theme.LocalPallet
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun UserRowComposable(
    user: BSBUser,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            modifier = Modifier
                .clip(RoundedCornerShape(32.dp))
                .background(LocalPallet.current.transparent.whiteInvert.quaternary)
                .padding(18.dp)
                .size(24.dp),
            painter = painterResource(Res.drawable.ic_user),
            contentDescription = null,
            tint = LocalPallet.current.transparent.whiteInvert.secondary
        )
        Text(
            modifier = Modifier.weight(1f),
            text = user.email,
            fontSize = 16.sp,
            fontFamily = LocalBusyBarFonts.current.ppNeueMontreal,
            fontWeight = FontWeight.W600,
            color = LocalPallet.current.white.invert
        )

        LogOutButtonComposable(
            onClick = onLogout
        )
    }
}

@Preview
@Composable
private fun PreviewUserRowComposable() {
    BusyBarThemeInternal {
        UserRowComposable(
            BSBUser("test@test.uk"),
            onLogout = {}
        )
    }
}
