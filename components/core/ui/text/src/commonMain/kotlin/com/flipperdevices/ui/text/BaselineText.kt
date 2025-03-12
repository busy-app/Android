@file:Suppress("UnstableCollections")

import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.isSpecified
import androidx.compose.ui.unit.takeOrElse

/**
 * Wrapper for the [Text] element which adds additional padding after the last baseline
 * such that the height of the last line of text matches the style's
 * [lineHeight][TextStyle.lineHeight].
 */
@Composable
fun BaselineText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    inlineContent: Map<String, InlineTextContent> = mapOf(),
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current
) = BaselineText(
    AnnotatedString(text),
    modifier,
    color,
    fontSize,
    fontStyle,
    fontWeight,
    fontFamily,
    letterSpacing,
    textDecoration,
    textAlign,
    lineHeight,
    overflow,
    softWrap,
    maxLines,
    inlineContent,
    onTextLayout,
    style
)

/**
 * Wrapper for the [Text] element which adds additional padding after the last baseline
 * such that the height of the last line of text matches the style's
 * [lineHeight][TextStyle.lineHeight].
 */
@Composable
fun BaselineText(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    inlineContent: Map<String, InlineTextContent> = mapOf(),
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current
) {
    val resolvedLineHeight = lineHeight.takeOrElse { style.lineHeight }
    val resolvedFontSize = fontSize.takeOrElse { style.fontSize }
    val paddedModifier = if (resolvedLineHeight.isSpecified && resolvedFontSize.isSpecified) {
        with(LocalDensity.current) {
            val lineHeightDp = resolvedLineHeight.toDp()
            val fontSizeDp = resolvedFontSize.toDp()
            Modifier.paddingFromBaseline(
                top = fontSizeDp,
                bottom = lineHeightDp - fontSizeDp
            )
        }
    } else {
        Modifier
    }
    Text(
        text = text,
        modifier = modifier.then(paddedModifier),
        color = color,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        inlineContent = inlineContent,
        onTextLayout = onTextLayout,
        style = style
    )
}
