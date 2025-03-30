package com.flipperdevices.bsb.timer.setup.api

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.composables.core.SheetDetent
import com.flipperdevices.bsb.dao.model.TimerSettingsId
import com.flipperdevices.bsb.timer.setup.model.TimerSetupScreenConfig
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.ui.decompose.DecomposeComponent
import com.flipperdevices.ui.decompose.DecomposeOnBackParameter
import com.flipperdevices.ui.sheet.BModalBottomSheetContent
import com.flipperdevices.ui.sheet.ModalBottomSheetSlot
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
class CardEditSheetDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    private val timerSetupSheetDecomposeComponentImpl: (
        componentContext: ComponentContext,
        timerSettingsId: TimerSettingsId,
        onBack: DecomposeOnBackParameter
    ) -> TimerSetupSheetDecomposeComponentImpl,
) : CardEditSheetDecomposeComponent(componentContext) {
    private val slot = SlotNavigation<TimerSetupScreenConfig>()
    private val childSlot = childSlot(
        source = slot,
        serializer = TimerSetupScreenConfig.serializer(),
        childFactory = ::childComponent
    )

    override fun show(timerSettingsId: TimerSettingsId) {
        slot.activate(TimerSetupScreenConfig.Main(timerSettingsId))
    }

    @Composable
    override fun Render(modifier: Modifier) {
        ModalBottomSheetSlot(
            slot = childSlot,
            initialDetent = SheetDetent.FullyExpanded,
            onDismiss = { slot.dismiss() },
            content = { config ->
                BModalBottomSheetContent(
                    horizontalPadding = 0.dp,
                    content = {
                        config.Render(Modifier)
                    }
                )
            }
        )
    }

    private fun childComponent(
        config: TimerSetupScreenConfig,
        componentContext: ComponentContext
    ): DecomposeComponent = when (config) {
        is TimerSetupScreenConfig.Main -> timerSetupSheetDecomposeComponentImpl(
            componentContext,
            config.timerSettingsId,
            { slot.dismiss() }
        )
    }

    @Inject
    @ContributesBinding(AppGraph::class, CardEditSheetDecomposeComponent.Factory::class)
    class Factory(
        private val factory: (
            componentContext: ComponentContext
        ) -> CardEditSheetDecomposeComponentImpl
    ) : CardEditSheetDecomposeComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext
        ) = factory(componentContext)
    }
}
