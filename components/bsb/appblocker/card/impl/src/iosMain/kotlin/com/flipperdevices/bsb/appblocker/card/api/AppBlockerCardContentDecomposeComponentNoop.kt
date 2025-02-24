package com.flipperdevices.bsb.appblocker.card.api

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import busystatusbar.components.bsb.appblocker.card.impl.generated.resources.Res
import busystatusbar.components.bsb.appblocker.card.impl.generated.resources.appblocker_wrong_platform
import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.ui.decompose.DecomposeOnBackParameter
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.jetbrains.compose.resources.stringResource
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
class AppBlockerCardContentDecomposeComponentNoop(
    @Assisted componentContext: ComponentContext,
    @Assisted private val onBackParameter: DecomposeOnBackParameter,
) : AppBlockerCardContentDecomposeComponent(componentContext) {
    @Composable
    override fun Render(modifier: Modifier) {
        Column(
            modifier.fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = stringResource(Res.string.appblocker_wrong_platform),
                fontSize = 16.sp,
                fontFamily = LocalBusyBarFonts.current.pragmatica,
                fontWeight = FontWeight.W500,
                color = LocalCorruptedPallet.current.neutral.tertiary
            )

            Spacer(Modifier.height(16.dp))
        }
    }

    @Inject
    @ContributesBinding(AppGraph::class, AppBlockerCardContentDecomposeComponent.Factory::class)
    class Factory(
        private val factory: (
            componentContext: ComponentContext,
            onBackParameter: DecomposeOnBackParameter
        ) -> AppBlockerCardContentDecomposeComponentNoop
    ) : AppBlockerCardContentDecomposeComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            onBackParameter: DecomposeOnBackParameter
        ) = factory(componentContext, onBackParameter)
    }
}
