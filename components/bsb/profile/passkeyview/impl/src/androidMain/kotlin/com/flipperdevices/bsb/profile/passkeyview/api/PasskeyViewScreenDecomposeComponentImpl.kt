package com.flipperdevices.bsb.profile.passkeyview.api

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import busystatusbar.components.bsb.profile.passkeyview.impl.generated.resources.Res
import busystatusbar.components.bsb.profile.passkeyview.impl.generated.resources.ic_passkey
import busystatusbar.components.bsb.profile.passkeyview.impl.generated.resources.profile_passkey_btn
import busystatusbar.components.bsb.profile.passkeyview.impl.generated.resources.profile_passkey_desc
import busystatusbar.components.bsb.profile.passkeyview.impl.generated.resources.profile_passkey_title
import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.bsb.common.ProfileButtonComposable
import com.flipperdevices.bsb.common.ProfileSectionTitleComposable
import com.flipperdevices.bsb.core.theme.LocalPallet
import com.flipperdevices.bsb.profile.passkeyview.model.PasskeyAddState
import com.flipperdevices.bsb.profile.passkeyview.viewmodel.PasskeyAddViewModel
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.ktx.common.clickableRipple
import com.flipperdevices.core.ui.lifecycle.viewModelWithFactory
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
class PasskeyViewScreenDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    private val viewModelFactory: () -> PasskeyAddViewModel
) : PasskeyViewScreenDecomposeComponent(componentContext) {
    private val viewModel = viewModelWithFactory(null) {
        viewModelFactory()
    }

    @Composable
    override fun Render(modifier: Modifier) {
        val state by viewModel.getState().collectAsState()
        Column(modifier) {
            ProfileSectionTitleComposable(
                modifier = Modifier.padding(vertical = 32.dp),
                title = Res.string.profile_passkey_title
            )

            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        LocalPallet.current.transparent.whiteInvert.quinary
                    )
                    .clickableRipple(onClick = viewModel::addPasskey)
                    .padding(vertical = 24.dp, horizontal = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Image(
                    modifier = Modifier.size(64.dp),
                    contentDescription = stringResource(Res.string.profile_passkey_title),
                    painter = painterResource(Res.drawable.ic_passkey)
                )

                Text(
                    text = stringResource(Res.string.profile_passkey_desc),
                    fontWeight = FontWeight.W400,
                    color = LocalPallet.current.white.invert,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )

                ProfileButtonComposable(
                    text = Res.string.profile_passkey_btn,
                    inProgress = state == PasskeyAddState.AddInProgress
                )
            }
        }
    }

    @Inject
    @ContributesBinding(AppGraph::class, PasskeyViewScreenDecomposeComponent.Factory::class)
    class Factory(
        private val factory: (
            componentContext: ComponentContext
        ) -> PasskeyViewScreenDecomposeComponentImpl
    ) : PasskeyViewScreenDecomposeComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext
        ) = factory(componentContext)
    }
}
