package com.flipperdevices.bsb.timer.setup.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import busystatusbar.components.bsb.timer.setup.impl.generated.resources.Res
import busystatusbar.components.bsb.timer.setup.impl.generated.resources.ic_block
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.composables.core.SheetDetent
import com.flipperdevices.bsb.timer.setup.composable.intervals.BlockedAppsSetupModalBottomSheetContent
import com.flipperdevices.bsb.timer.setup.composable.intervals.LongRestSetupModalBottomSheetContent
import com.flipperdevices.bsb.timer.setup.composable.intervals.RestSetupModalBottomSheetContent
import com.flipperdevices.bsb.timer.setup.composable.intervals.SoundSetupModalBottomSheetContent
import com.flipperdevices.bsb.timer.setup.composable.intervals.WorkSetupModalBottomSheetContent
import com.flipperdevices.bsb.timer.setup.viewmodel.TimerSetupViewModel
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.ui.sheet.BModalBottomSheetContent
import com.flipperdevices.ui.sheet.ModalBottomSheetSlot
import kotlinx.serialization.Serializable
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.jetbrains.compose.resources.painterResource
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
class IntervalsSetupSheetDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    timerSetupViewModelFactory: () -> TimerSetupViewModel
) : IntervalsSetupSheetDecomposeComponent(componentContext) {
    private val timerSetupViewModel = instanceKeeper.getOrCreate {
        timerSetupViewModelFactory.invoke()
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

        @Serializable
        data object Sound : PickerConfiguration

        @Serializable
        data object BlockedApps : PickerConfiguration
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

    override fun showSound() {
        slot.activate(PickerConfiguration.Sound)
    }

    override fun showBlockedApps() {
        slot.activate(PickerConfiguration.BlockedApps)
    }

    // todo This method will be rewritten with another design
    @Suppress("LongMethod")
    @Composable
    override fun Render(modifier: Modifier) {
        val timerSettings = timerSetupViewModel.state.collectAsState()
        ModalBottomSheetSlot(
            slot = childSlot,
            initialDetent = SheetDetent.FullyExpanded,
            onDismiss = slot::dismiss,
            content = {
                when (it) {
                    PickerConfiguration.Work -> {
                        BModalBottomSheetContent(horizontalPadding = 0.dp) {
                            WorkSetupModalBottomSheetContent(
                                timerSettings = timerSettings.value,
                                onSaveClick = slot::dismiss,
                                onTimeChange = timerSetupViewModel::setWork,
                                onAutoStartToggle = timerSetupViewModel::toggleWorkAutoStart,
                            )
                        }
                    }

                    PickerConfiguration.LongRest -> {
                        BModalBottomSheetContent(horizontalPadding = 0.dp) {
                            LongRestSetupModalBottomSheetContent(
                                timerSettings = timerSettings.value,
                                onSaveClick = slot::dismiss,
                                onTimeChange = timerSetupViewModel::setLongRest,
                            )
                        }
                    }

                    PickerConfiguration.Rest -> {
                        BModalBottomSheetContent(horizontalPadding = 0.dp) {
                            RestSetupModalBottomSheetContent(
                                timerSettings = timerSettings.value,
                                onSaveClick = slot::dismiss,
                                onTimeChange = timerSetupViewModel::setRest,
                                onAutoStartToggle = timerSetupViewModel::toggleRestAutoStart,
                            )
                        }
                    }

                    PickerConfiguration.Sound -> {
                        BModalBottomSheetContent(horizontalPadding = 0.dp) {
                            SoundSetupModalBottomSheetContent(
                                timerSettings = timerSettings.value,
                                onSaveClick = slot::dismiss,
                                onAlertBeforeWorkStartsToggle = timerSetupViewModel::toggleSoundBeforeWorkStarts,
                                onAlertBeforeWorkEndsToggle = timerSetupViewModel::toggleSoundBeforeWorkEnds
                            )
                        }
                    }

                    PickerConfiguration.BlockedApps -> {
                        BModalBottomSheetContent(horizontalPadding = 0.dp) {
                            BlockedAppsSetupModalBottomSheetContent(
                                onSaveClick = slot::dismiss,
                                blockedAppsDuringWork = emptyList(),
                                blockedAppsDuringRest = List(24) { painterResource(Res.drawable.ic_block) }
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
            componentContext: ComponentContext
        ) -> IntervalsSetupSheetDecomposeComponentImpl
    ) : IntervalsSetupSheetDecomposeComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext
        ) = factory(componentContext)
    }
}
