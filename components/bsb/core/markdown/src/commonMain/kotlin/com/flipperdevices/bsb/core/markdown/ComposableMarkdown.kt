package com.flipperdevices.bsb.core.markdown

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import com.flipperdevices.bsb.core.theme.LocalPallet
import com.mikepenz.markdown.compose.Markdown
import com.mikepenz.markdown.model.DefaultMarkdownColors
import com.mikepenz.markdown.model.DefaultMarkdownTypography
import com.mikepenz.markdown.model.MarkdownColors
import com.mikepenz.markdown.model.MarkdownPadding
import com.mikepenz.markdown.model.MarkdownTypography
import com.mikepenz.markdown.model.markdownPadding

@Composable
fun ComposableMarkdown(
    content: String,
    modifier: Modifier = Modifier,
    typography: MarkdownTypography = markdownTypography(),
    colors: MarkdownColors = markdownColor(),
    paddings: MarkdownPadding = markdownPadding(
        block = 2.dp,
        indentList = 4.dp,
        list = 1.dp
    )
) {
    Markdown(
        modifier = modifier,
        content = content,
        colors = colors,
        typography = typography,
        padding = paddings
    )
}

@Composable
fun markdownColor(
    defaultColor: Color = LocalPallet.current.neutral.tertiary
): MarkdownColors = DefaultMarkdownColors(
    text = defaultColor,
    codeText = defaultColor,
    inlineCodeText = defaultColor,
    linkText = LocalPallet.current.bluetooth.primary,
    codeBackground = defaultColor,
    inlineCodeBackground = defaultColor,
    dividerColor = defaultColor,
    tableText = Color.Unspecified,
    tableBackground = defaultColor.copy(alpha = 0.5f),
)

@Composable
fun markdownTypography(
    defaultTypography: TextStyle = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.W500,
        fontFamily = LocalBusyBarFonts.current.ppNeueMontreal,
    )
): MarkdownTypography = DefaultMarkdownTypography(
    h1 = defaultTypography,
    h2 = defaultTypography,
    h3 = defaultTypography,
    h4 = defaultTypography,
    h5 = defaultTypography,
    h6 = defaultTypography,
    text = defaultTypography,
    code = defaultTypography,
    inlineCode = defaultTypography,
    quote = defaultTypography,
    paragraph = defaultTypography,
    ordered = defaultTypography,
    bullet = defaultTypography,
    list = defaultTypography,
    link = defaultTypography.copy(
        textDecoration = TextDecoration.Underline,
    ),
    textLink = TextLinkStyles(
        style = defaultTypography.copy(
            textDecoration = TextDecoration.Underline,
        ).toSpanStyle()
    )
)
