package com.flipperdevices.bsb.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.Colors
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.contentColorFor
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.stack.animation.LocalStackAnimationProvider
import com.flipperdevices.bsb.core.theme.generated.BusyBarPallet
import com.flipperdevices.bsb.core.theme.generated.getDarkPallet
import com.flipperdevices.bsb.core.theme.generated.getLightPallet
import com.flipperdevices.bsb.core.theme.generated.toAnimatePallet

val LocalPallet = compositionLocalOf<BusyBarPallet> { error("No local pallet") }

@Composable
private fun getThemedPallet(isLight: Boolean): BusyBarPallet {
    return remember(isLight) {
        if (isLight) {
            getLightPallet()
        } else {
            getDarkPallet()
        }
    }.toAnimatePallet()
}

internal fun BusyBarPallet.toMaterialColors(isLight: Boolean) = Colors(
    primary = accent.device.primary,
    primaryVariant = accent.device.primary,
    secondary = accent.device.secondary,
    secondaryVariant = accent.device.secondary,
    background = surface.primary,
    surface = surface.primary,
    error = danger.primary,
    onPrimary = white.onColor,
    onSecondary = black.onColor,
    onBackground = black.onColor,
    onSurface = black.onColor,
    onError = white.onColor,
    isLight = isLight
)

internal fun BusyBarPallet.toMaterial3Scheme(isLight: Boolean): ColorScheme {
    val scheme = if (isLight) lightColorScheme() else darkColorScheme()
    return scheme.copy(
        primary = accent.device.primary,
        onPrimary = white.onColor,
//        primaryContainer = Color.Blue,
//        onPrimaryContainer = Color.Green,
//        inversePrimary = Color.Magenta,
        secondary = accent.device.secondary,
//        onSecondary = Color.Red,
//        secondaryContainer = Color.Green,
//        onSecondaryContainer = Color.Magenta,
//        tertiary = Color.Yellow,
//        onTertiary = Color.Red,
//        tertiaryContainer = Color.Green,
//        onTertiaryContainer = Color.Magenta,
//        background = Color.Magenta,
//        onBackground = Color.Green,
//        surface = Color.Red,
//        onSurface = Color.Magenta,
//        surfaceVariant = Color.Yellow,
//        onSurfaceVariant = Color.Cyan,
//        surfaceTint = Color.Green,
//        inverseSurface = Color.Red,
//        inverseOnSurface = Color.Magenta,
//        error = Color.Yellow,
//        onError = Color.Cyan,
        errorContainer = Color.Red,
        onErrorContainer = Color.Green,
        outline = transparent.whiteInvert.tertiary,
        outlineVariant = Color.Yellow,
        scrim = Color.Cyan,
//        surfaceBright = Color.Red,
//        surfaceDim = Color.Green,
//        surfaceContainer = Color.Magenta,
//        surfaceContainerHigh = Color.Yellow,
        surfaceContainerHighest = transparent.whiteInvert.tertiary,
//        surfaceContainerLow = Color.Red,
//        surfaceContainerLowest = Color.Green,
    )
}

internal fun BusyBarPallet.toTextSelectionColors() = TextSelectionColors(
    handleColor = accent.device.primary,
    backgroundColor = surface.primary
)

@Composable
fun BusyBarTheme(
    darkMode: Boolean,
    content: @Composable () -> Unit
) {
    val isLight = if (darkMode) {
        false
    } else {
        isSystemInDarkTheme().not()
    }
    BusyBarThemeInternal(
        isLight = isLight,
        content = content
    )
}

@Composable
fun BusyBarThemeInternal(
    isLight: Boolean = true,
    content: @Composable () -> Unit,
) {
    val isLight = true // todo there's something wrong with dark theme colors at the moment
    val pallet = getThemedPallet(
        isLight = isLight
    )
    val colors = pallet.toMaterialColors(isLight)
    val m3Colors = pallet.toMaterial3Scheme(isLight)
    val shapes = Shapes(medium = RoundedCornerShape(size = 10.dp))
    val fonts = getBusyBarFonts()

    androidx.compose.material3.MaterialTheme(
        colorScheme = m3Colors,
    ) {
        MaterialTheme(
            shapes = shapes,
            colors = colors
        ) {
            CompositionLocalProvider(
                LocalPallet provides pallet,
                LocalBusyBarFonts provides fonts,
                LocalContentColor provides colors.contentColorFor(backgroundColor = colors.background),
                LocalTextSelectionColors provides pallet.toTextSelectionColors(),
                LocalStackAnimationProvider provides BusyBarAnimationProvider,
                content = content
            )
        }
    }
}
