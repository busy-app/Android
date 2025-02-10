package com.flipperdevices.ui.options

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalPallet
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun OptionComposable(
    text: String,
    end: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    icon: Painter? = null,
    iconTint: Color = LocalPallet.current
        .transparent
        .whiteInvert
        .secondary
        .copy(alpha = 0.3f),
    infoText: String? = null,
    onClick: (() -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .then(modifier),
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
                color = LocalPallet.current
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
                    color = LocalPallet.current
                        .transparent
                        .whiteInvert
                        .secondary
                        .copy(alpha = 0.3f),
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Start,
                )
            }
        }
        end.invoke()
    }
}

@Composable
@Preview
@Suppress("LongMethod")
private fun OptionComposablePreview() {
    val modifier = Modifier.padding(
        horizontal = 8.dp,
        vertical = 4.dp
    )
    BusyBarThemeInternal {
        Column {
            OptionComposable(
                icon = rememberVectorPainter(Icons.Default.Call),
                text = TEXT,
                onClick = {},
                modifier = modifier,
                end = {
                    Text("Hello!")
                }
            )
            OptionSeparator(Modifier.fillMaxWidth())
            OptionComposable(
                icon = rememberVectorPainter(Icons.Default.Call),
                text = LONG_TEXT,
                onClick = {},
                modifier = modifier,
                end = {
                    Text("Hello!")
                }
            )
            OptionSeparator(Modifier.fillMaxWidth())
            OptionComposable(
                icon = rememberVectorPainter(Icons.Default.Call),
                text = TEXT,
                infoText = TEXT,
                onClick = {},
                modifier = modifier,
                end = {
                    Text("Hello!")
                }
            )
            OptionSeparator(Modifier.fillMaxWidth())
            OptionComposable(
                icon = rememberVectorPainter(Icons.Default.Call),
                text = LONG_TEXT,
                infoText = TEXT,
                onClick = {},
                modifier = modifier,
                end = {
                    Text("Hello!")
                }
            )
            OptionSeparator(Modifier.fillMaxWidth())
            OptionComposable(
                icon = rememberVectorPainter(Icons.Default.Call),
                text = LONG_TEXT,
                infoText = LONG_TEXT,
                onClick = {},
                modifier = modifier,
                end = {
                    Text("Hello!")
                }
            )
            OptionSeparator(Modifier.fillMaxWidth())
            OptionComposable(
                icon = rememberVectorPainter(Icons.Default.Call),
                text = TEXT,
                infoText = LONG_TEXT,
                onClick = {},
                modifier = modifier,
                end = {
                    Text("Hello!")
                }
            )
        }
    }
}
