package com.flipperdevices.ui.options

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import busystatusbar.components.core.ui.res_preview.generated.resources.ic_preview_pomodoro
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import busystatusbar.components.core.ui.res_preview.generated.resources.Res as PreviewRes

@Composable
fun OptionInfo(
    text: String,
    endText: String,
    modifier: Modifier = Modifier,
    icon: Painter? = null,
    infoText: String? = null,
    iconTint: Color = MaterialTheme.colors.onPrimary,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(
            8.dp,
            Alignment.End
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon?.let { icon ->
            Icon(
                painter = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )
        }
        Column(
            modifier = Modifier.weight(1f, true),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                color = LocalCorruptedPallet.current
                    .transparent
                    .whiteInvert
                    .primary
                    .copy(alpha = 0.5f),
                textAlign = TextAlign.Start,
                fontSize = 18.sp
            )
            infoText?.let {
                Text(
                    text = infoText,
                    color = LocalCorruptedPallet.current
                        .transparent
                        .whiteInvert
                        .secondary
                        .copy(alpha = 0.3f),
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Start,
                )
            }
        }
        Text(
            text = endText,
            color = LocalCorruptedPallet.current
                .transparent
                .whiteInvert
                .primary
                .copy(alpha = 0.5f),
            fontSize = 18.sp,
            textAlign = TextAlign.End,
        )
    }
}

@Composable
@Preview
private fun OptionInfoPreview() {
    val modifier = Modifier.padding(
        horizontal = 8.dp,
        vertical = 4.dp
    )
    BusyBarThemeInternal {
        Column {
            OptionInfo(
                icon = painterResource(PreviewRes.drawable.ic_preview_pomodoro),
                text = TEXT,
                endText = TEXT,
                modifier = modifier
            )
            OptionSeparator(Modifier.fillMaxWidth())
            OptionInfo(
                icon = painterResource(PreviewRes.drawable.ic_preview_pomodoro),
                text = LONG_TEXT,
                endText = TEXT,
                modifier = modifier
            )
            OptionSeparator(Modifier.fillMaxWidth())
            OptionInfo(
                icon = painterResource(PreviewRes.drawable.ic_preview_pomodoro),
                text = TEXT,
                infoText = TEXT,
                endText = TEXT,
                modifier = modifier
            )
            OptionSeparator(Modifier.fillMaxWidth())
            OptionInfo(
                icon = painterResource(PreviewRes.drawable.ic_preview_pomodoro),
                text = LONG_TEXT,
                infoText = TEXT,
                endText = TEXT,
                modifier = modifier
            )
            OptionSeparator(Modifier.fillMaxWidth())
            OptionInfo(
                icon = painterResource(PreviewRes.drawable.ic_preview_pomodoro),
                text = LONG_TEXT,
                infoText = LONG_TEXT,
                endText = TEXT,
                modifier = modifier
            )
            OptionSeparator(Modifier.fillMaxWidth())
            OptionInfo(
                icon = painterResource(PreviewRes.drawable.ic_preview_pomodoro),
                text = TEXT,
                infoText = LONG_TEXT,
                endText = TEXT,
                modifier = modifier
            )
        }
    }
}
