package com.flipperdevices.ui.autosizetext

import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.util.fastFilter

enum class SuggestedFontSizesStatus {
    VALID, INVALID, UNKNOWN;

    companion object {
        val List<TextUnit>.validSuggestedFontSizes
            get() = takeIf { it.isNotEmpty() } // Optimization: empty check first to immediately return null
                ?.fastFilter { it.isSp }
                ?.takeIf { it.isNotEmpty() }
                ?.sortedBy { it.value }
    }
}
