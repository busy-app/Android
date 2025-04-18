package com.flipperdevices.bsb.timer.setup.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.composables.core.SheetDetent
import com.flipperdevices.bsb.dao.model.TimerSettingsId
import com.flipperdevices.bsb.timer.setup.composable.intervals.LongRestSetupModalBottomSheetContent
import com.flipperdevices.bsb.timer.setup.composable.intervals.RestSetupModalBottomSheetContent
import com.flipperdevices.bsb.timer.setup.composable.intervals.WorkSetupModalBottomSheetContent
import com.flipperdevices.bsb.timer.setup.model.CardEditScreenState
import com.flipperdevices.bsb.timer.setup.viewmodel.TimerSetupViewModel
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.core.ui.lifecycle.viewModelWithFactory
import com.flipperdevices.ui.sheet.BModalBottomSheetContent
import com.flipperdevices.ui.sheet.ModalBottomSheetSlot
import kotlinx.serialization.Serializable
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
class IntervalsSetupSheetDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    @Assisted timerSettingsId: TimerSettingsId,
    timerSetupViewModelFactory: (TimerSettingsId) -> TimerSetupViewModel
) : IntervalsSetupSheetDecomposeComponent(componentContext) {
    private val timerSetupViewModel = viewModelWithFactory(timerSettingsId) {
        timerSetupViewModelFactory.invoke(timerSettingsId)
    }

    private val slot = SlotNavigation<PickerConfiguration>()
    private val childSlot = childSlot(
        source = slot,
        serializer = PickerConfiguration.serializer(),
        childFactory = { configuration, context ->
            configuration
        }
    )

    @Serializable
    private sealed interface PickerConfiguration {
        @Serializable
        data object Work : PickerConfiguration

        @Serializable
        data object Rest : PickerConfiguration

        @Serializable
        data object LongRest : PickerConfiguration
    }

    override fun showRest() {
        slot.activate(PickerConfiguration.Rest)
    }

    override fun showLongRest() {
        slot.activate(PickerConfiguration.LongRest)
    }

    override fun showWork() {
        slot.activate(PickerConfiguration.Work)
    }

    @Suppress("LongMethod")
    @Composable
    override fun Render(modifier: Modifier) {
        val state by timerSetupViewModel.getState().collectAsState()
        val timerSettings = when (val localState = state) {
            CardEditScreenState.NotInitialized -> return
            is CardEditScreenState.Loaded -> localState.timerSettings ?: return
        }

        ModalBottomSheetSlot(
            slot = childSlot,
            initialDetent = SheetDetent.FullyExpanded,
            onDismiss = slot::dismiss,
            content = {
                when (it) {
                    PickerConfiguration.Work -> {
                        BModalBottomSheetContent(horizontalPadding = 0.dp) {
                            WorkSetupModalBottomSheetContent(
                                timerSettings = timerSettings,
                                onSaveClick = slot::dismiss,
                                onTimeChange = timerSetupViewModel::setWork,
                                onAutoStartToggle = timerSetupViewModel::toggleWorkAutoStart,
                            )
                        }
                    }

                    PickerConfiguration.LongRest -> {
                        BModalBottomSheetContent(horizontalPadding = 0.dp) {
                            LongRestSetupModalBottomSheetContent(
                                timerSettings = timerSettings,
                                onSaveClick = slot::dismiss,
                                onTimeChange = timerSetupViewModel::setLongRest,
                            )
                        }
                    }

                    PickerConfiguration.Rest -> {
                        BModalBottomSheetContent(horizontalPadding = 0.dp) {
                            RestSetupModalBottomSheetContent(
                                timerSettings = timerSettings,
                                onSaveClick = slot::dismiss,
                                onTimeChange = timerSetupViewModel::setRest,
                                onAutoStartToggle = timerSetupViewModel::toggleRestAutoStart,
                            )
                        }
                    }
                }
            }
        )
    }

    @Inject
    @ContributesBinding(AppGraph::class, IntervalsSetupSheetDecomposeComponent.Factory::class)
    class Factory(
        private val factory: (
            componentContext: ComponentContext,
            timerSettingsId: TimerSettingsId,
        ) -> IntervalsSetupSheetDecomposeComponentImpl
    ) : IntervalsSetupSheetDecomposeComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            timerSettingsId: TimerSettingsId
        ) = factory(componentContext, timerSettingsId)
    }
}
