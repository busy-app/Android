package com.flipperdevices.ui.timeline.util

import com.flipperdevices.ui.timeline.model.PickerLineStyle

val PickerLineStyle.stepAndSelectedLineDiff get() = selectedLineHeight - stepLineHeight
val PickerLineStyle.normalAndSelectedLineDiff get() = selectedLineHeight - normalLineHeight

val PickerLineStyle.selectedAndSelectedZeroFontDiff get() = (selectedZeroFontSize - selectedFontSize)
val PickerLineStyle.unselectedAndSelectedFontDiff get() = (selectedFontSize - unselectedFontSize)
val PickerLineStyle.unselectedAndSelectedZeroFontDiff get() = (selectedZeroFontSize - unselectedFontSize)
