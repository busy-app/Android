package com.flipperdevices.bsb.profile.main.api

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.flipperdevices.bsb.cloud.model.BSBUser
import com.flipperdevices.bsb.profile.main.composable.SeparatorComposable
import com.flipperdevices.bsb.profile.main.composable.UserRowComposable
import com.flipperdevices.bsb.profile.passkeyview.api.PasskeyViewScreenDecomposeComponent
import com.flipperdevices.ui.decompose.ScreenDecomposeComponent
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class ProfileMainDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    @Assisted private val bsbUser: BSBUser,
    @Assisted private val onLogout: () -> Unit,
    passkeyViewFactory: PasskeyViewScreenDecomposeComponent.Factory
) : ScreenDecomposeComponent(componentContext) {
    private val passkeyView = passkeyViewFactory(
        componentContext = childContext("profile_passkeyView")
    )

    @Composable
    override fun Render(modifier: Modifier) {
        Column(
            modifier
                .background(Color(color = 0x212121))
                .safeDrawingPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            UserRowComposable(
                modifier = Modifier.padding(bottom = 64.dp),
                user = bsbUser,
                onLogout = onLogout
            )

            SeparatorComposable()

            passkeyView.Render(Modifier)
        }
    }
}
