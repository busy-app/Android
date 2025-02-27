package com.flipperdevices.bsbwearable.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.flipperdevices.bsb.core.theme.BusyBarTheme
import com.flipperdevices.bsb.core.theme.LocalPallet
import com.flipperdevices.bsbwearable.di.WearAppComponent
import com.flipperdevices.bsbwearable.root.api.RootDecomposeComponent

@Composable
@Suppress("UnusedParameter")
internal fun WearApp(
    rootComponent: RootDecomposeComponent,
    appComponent: WearAppComponent,
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
