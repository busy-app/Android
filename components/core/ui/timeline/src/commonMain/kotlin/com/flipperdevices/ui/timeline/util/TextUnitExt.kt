package com.flipperdevices.ui.timeline.util

import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

internal operator fun TextUnit.plus(other: TextUnit): TextUnit {
    return this.value.plus(other.value).sp
}

internal operator fun TextUnit.minus(other: TextUnit): TextUnit {
    return this.value.minus(other.value).sp
}
