package com.flipperdevices.ui.options

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import busystatusbar.components.core.ui.option.generated.resources.Res
import busystatusbar.components.core.ui.option.generated.resources.ic_arrow_right
import busystatusbar.components.core.ui.res_preview.generated.resources.ic_preview_pomodoro
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import busystatusbar.components.core.ui.res_preview.generated.resources.Res as PreviewRes

@Composable
fun OptionHref(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: Painter? = null,
    iconTint: Color = LocalCorruptedPallet.current
        .transparent
        .whiteInvert
        .secondary
        .copy(alpha = 0.3f),
    infoText: String? = null,
    isEnabled: Boolean = true,
) {
    Row(
        modifier = Modifier.clickable { onClick.invoke() }.then(modifier),
        horizontalArrangement = Arrangement.spacedBy(
            8.dp,
            Alignment.CenterHorizontally
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
            modifier = Modifier.weight(1f),
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
        Icon(
            painter = painterResource(Res.drawable.ic_arrow_right),
            contentDescription = null,
            tint = LocalCorruptedPallet.current
                .transparent
                .whiteInvert
                .secondary
                .copy(alpha = 0.3f),
            modifier = Modifier.size(24.dp)
                .clip(CircleShape)
                .clickable(enabled = isEnabled) { onClick.invoke() }
        )
    }
}

internal const val TEXT = "Some text"
internal val LONG_TEXT = List(size = 10) {
    TEXT
}.joinToString(" ", "", "")

@Composable
@Preview
private fun OptionHrefPreview() {
    val modifier = Modifier.padding(
        horizontal = 8.dp,
        vertical = 4.dp
    )
    BusyBarThemeInternal {
        Column {
            OptionHref(
                icon = painterResource(PreviewRes.drawable.ic_preview_pomodoro),
                text = TEXT,
                onClick = {},
                modifier = modifier
            )
            OptionSeparator(Modifier.fillMaxWidth())
            OptionHref(
                icon = painterResource(PreviewRes.drawable.ic_preview_pomodoro),
                text = LONG_TEXT,
                onClick = {},
                modifier = modifier
            )
            OptionSeparator(Modifier.fillMaxWidth())
            OptionHref(
                icon = painterResource(PreviewRes.drawable.ic_preview_pomodoro),
                text = TEXT,
                infoText = TEXT,
                onClick = {},
                modifier = modifier
            )
            OptionSeparator(Modifier.fillMaxWidth())
            OptionHref(
                icon = painterResource(PreviewRes.drawable.ic_preview_pomodoro),
                text = LONG_TEXT,
                infoText = TEXT,
                onClick = {},
                modifier = modifier
            )
            OptionSeparator(Modifier.fillMaxWidth())
            OptionHref(
                icon = painterResource(PreviewRes.drawable.ic_preview_pomodoro),
                text = LONG_TEXT,
                infoText = LONG_TEXT,
                onClick = {},
                modifier = modifier
            )
            OptionSeparator(Modifier.fillMaxWidth())
            OptionHref(
                icon = painterResource(PreviewRes.drawable.ic_preview_pomodoro),
                text = TEXT,
                infoText = LONG_TEXT,
                onClick = {},
                modifier = modifier
            )
        }
    }
}
