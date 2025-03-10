package com.flipperdevices.bsbwearable.interrupt.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.flipperdevices.bsb.timer.background.api.TimerApi
import com.flipperdevices.bsb.timer.background.util.stop
import com.flipperdevices.bsbwearable.interrupt.composable.ConfirmStopOverlayComposable
import com.flipperdevices.core.di.AppGraph
import kotlinx.serialization.builtins.serializer
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
class StopSessionDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    private val timerApi: TimerApi
) : StopSessionDecomposeComponent(componentContext) {

    private val slot = SlotNavigation<Unit>()
    private val childSlot = childSlot(
        source = slot,
        serializer = Unit.serializer(),
        childFactory = { configuration, context ->
            configuration
        }
    )

    override fun show() {
        slot.activate(Unit)
    }

    @Composable
    override fun Render(modifier: Modifier) {
        val child by childSlot.subscribeAsState()
        child.child?.instance?.let {
            ConfirmStopOverlayComposable(
                onStopClick = {
                    timerApi.stop()
                },
                onDismiss = { slot.dismiss() }
            )
        }
    }

    @Inject
    @ContributesBinding(AppGraph::class, StopSessionDecomposeComponent.Factory::class)
    class Factory(
        private val factory: (
            componentContext: ComponentContext,
        ) -> StopSessionDecomposeComponentImpl
    ) : StopSessionDecomposeComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
        ) = factory(componentContext)
    }
}
