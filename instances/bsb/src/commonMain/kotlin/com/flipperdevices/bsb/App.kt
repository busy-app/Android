package com.flipperdevices.bsb

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.flipperdevices.bsb.core.theme.BusyBarTheme
import com.flipperdevices.bsb.core.theme.LocalPallet
import com.flipperdevices.bsb.di.AppComponent
import com.flipperdevices.bsb.root.api.RootDecomposeComponent

@Composable
@Suppress("UnusedParameter")
fun App(
    rootComponent: RootDecomposeComponent,
    appComponent: AppComponent,
    modifier: Modifier = Modifier
) {
    BusyBarTheme(darkMode = true) {
        rootComponent.Render(
            modifier
                .fillMaxSize()
                .background(LocalPallet.current.black.onColor)
        )
    }
}
