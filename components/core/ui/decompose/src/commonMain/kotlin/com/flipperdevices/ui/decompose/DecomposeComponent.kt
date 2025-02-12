package com.flipperdevices.ui.decompose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

abstract class DecomposeComponent internal constructor() {
    @Composable
    abstract fun Render(modifier: Modifier)
}
