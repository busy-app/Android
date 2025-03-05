package com.flipperdevices.ui.autosizetext

import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.isSpecified

// Those function are designed to be used in lambdas

// TEXT UNIT
fun Density.spToIntPx(sp: TextUnit) = if (sp.isSpecified) sp.toPx().toInt() else 0

fun Density.spRoundToPx(sp: TextUnit) = if (sp.isSpecified) sp.roundToPx() else 0

fun Density.intPxToSp(px: Int) = px.toSp()

fun Density.dpSizeRoundToIntSize(dpSize: DpSize) =
    if (dpSize.isSpecified) {
        IntSize(dpSize.width.roundToPx(), dpSize.height.roundToPx())
    } else {
        IntSize.Zero
    }
