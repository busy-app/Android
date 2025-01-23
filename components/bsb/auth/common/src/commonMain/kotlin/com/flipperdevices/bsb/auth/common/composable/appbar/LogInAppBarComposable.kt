package com.flipperdevices.bsb.auth.common.composable.appbar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import busystatusbar.components.bsb.core.res.generated.resources.Res
import busystatusbar.components.bsb.core.res.generated.resources.ic_back
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import com.flipperdevices.bsb.core.theme.LocalPallet
import com.flipperdevices.core.ktx.common.clickableRipple
import com.flipperdevices.ui.appbar.ThreeBlockAppBarComposable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun LogInAppBarComposable(
    text: StringResource,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null
) {
    ThreeBlockAppBarComposable(
        modifier = modifier,
        startBlock = { startBlockModifier ->
            if (onBack != null) {
                Icon(
                    modifier = startBlockModifier
                        .clickableRipple(onClick = onBack, bounded = false)
                        .size(24.dp),
                    painter = painterResource(Res.drawable.ic_back),
                    contentDescription = null,
                    tint = LocalPallet.current.black.invert,
                )
            }
        },
        centerBlock = { centerBlockModifier ->
            Text(
                modifier = centerBlockModifier.padding(
                    horizontal = 16.dp,
                    vertical = 12.dp
                ).fillMaxWidth(),
                text = stringResource(text),
                fontFamily = LocalBusyBarFonts.current.ppNeueMontreal,
                fontWeight = FontWeight.W500,
                color = LocalPallet.current.black.invert,
                textAlign = TextAlign.Center,
                fontSize = 24.sp
            )
        }
    )
}
