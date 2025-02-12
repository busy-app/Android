package com.flipperdevices.bsb.auth.within.passkey.api

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import busystatusbar.components.bsb.auth.within.passkey.impl.generated.resources.Res
import busystatusbar.components.bsb.auth.within.passkey.impl.generated.resources.ic_passkey
import busystatusbar.components.bsb.auth.within.passkey.impl.generated.resources.passkey_title
import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.bsb.auth.within.main.model.AuthWay
import com.flipperdevices.bsb.auth.within.main.model.SignWithInState
import com.flipperdevices.bsb.auth.within.main.model.SignWithInStateListener
import com.flipperdevices.bsb.auth.within.passkey.viewmodel.PasskeyViewModel
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import com.flipperdevices.bsb.core.theme.LocalPallet
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.ktx.common.clickableRipple
import com.flipperdevices.core.ui.lifecycle.viewModelWithFactory
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
class PasskeyAuthDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    @Assisted withInStateListener: SignWithInStateListener,
    passkeyViewModelFactory: (withInStateListener: SignWithInStateListener) -> PasskeyViewModel
) : PasskeyAuthDecomposeComponent(componentContext) {
    private val viewModel = viewModelWithFactory(withInStateListener) {
        passkeyViewModelFactory(withInStateListener)
    }

    @Composable
    override fun Render(
        modifier: Modifier,
        authState: SignWithInState
    ) {
        Row(
            modifier
                .clickableRipple {
                    if (authState == SignWithInState.WaitingForInput) {
                        viewModel.onAuth()
                    }
                }
                .padding(vertical = 12.dp, horizontal = 32.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(Res.drawable.ic_passkey),
                contentDescription = stringResource(Res.string.passkey_title),
                tint = LocalPallet.current.black.invert
            )

            Text(
                text = stringResource(Res.string.passkey_title),
                fontSize = 16.sp,
                fontWeight = FontWeight.W500,
                fontFamily = LocalBusyBarFonts.current.ppNeueMontreal,
                color = LocalPallet.current.black.invert
            )

            if (authState is SignWithInState.InProgress &&
                authState.authWay == AuthWay.PASSKEY
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(24.dp)
                        .padding(1.2.dp),
                    color = LocalPallet.current.black.invert,
                    backgroundColor = LocalPallet.current.transparent.blackInvert.secondary,
                    strokeWidth = 2.dp
                )
            }
        }
    }

    @Inject
    @ContributesBinding(AppGraph::class, PasskeyAuthDecomposeComponent.Factory::class)
    class Factory(
        private val factory: (
            componentContext: ComponentContext,
            withInStateListener: SignWithInStateListener
        ) -> PasskeyAuthDecomposeComponentImpl
    ) : PasskeyAuthDecomposeComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            withInStateListener: SignWithInStateListener,
        ) = factory(componentContext, withInStateListener)
    }
}
