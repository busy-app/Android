package com.flipperdevices.bsb.timer.setup.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.composables.core.SheetDetent
import com.flipperdevices.bsb.timer.setup.composable.PickerContent
import com.flipperdevices.bsb.timer.setup.viewmodel.TimerSetupViewModel
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.ui.picker.rememberTimerState
import com.flipperdevices.ui.sheet.BModalBottomSheetContent
import com.flipperdevices.ui.sheet.ModalBottomSheetSlot
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import kotlin.time.Duration.Companion.minutes

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
        data object Rest : PickerConfiguration

        @Serializable
        data object LongRest : PickerConfiguration

        @Serializable
        data object Cycles : PickerConfiguration
    }

    override fun showRest() {
        slot.activate(PickerConfiguration.Rest)
    }

    override fun showLongRest() {
        slot.activate(PickerConfiguration.LongRest)
    }

    override fun showCycles() {
        slot.activate(PickerConfiguration.Cycles)
    }

    // todo This method will be rewritten with another design
    @Suppress("LongMethod")
    @Composable
    override fun Render(modifier: Modifier) {
        val state = timerSetupViewModel.state.collectAsState()
        ModalBottomSheetSlot(
            slot = childSlot,
            initialDetent = SheetDetent.FullyExpanded,
            onDismiss = { slot.dismiss() },
            content = {
                when (it) {
                    PickerConfiguration.Cycles -> {
                        BModalBottomSheetContent {
                            PickerContent(
                                title = "Cycles",
                                desc = "Pick how many focus and rest cycles you want to complete during your session",
                                onSaveClick = { value ->
                                    timerSetupViewModel.setCycles(value)
                                    slot.dismiss()
                                },
                                numberSelectorState = rememberTimerState(
                                    intProgression = 0..10 step 1,
                                    initialValue = state.value.intervalsSettings.cycles
                                )
                            )
                        }
                    }

                    PickerConfiguration.LongRest -> {
                        BModalBottomSheetContent {
                            PickerContent(
                                title = "Long rest",
                                desc = "Pick how long you want to relax after completing several cycles",
                                postfix = "min",
                                onSaveClick = { value ->
                                    timerSetupViewModel.setLongRest(value.minutes)
                                    slot.dismiss()
                                },
                                numberSelectorState = rememberTimerState(
                                    intProgression = 0..60 step 5,
                                    initialValue = state.value.intervalsSettings.longRest.inWholeMinutes.toInt()
                                )
                            )
                        }
                    }

                    PickerConfiguration.Rest -> {
                        BModalBottomSheetContent {
                            PickerContent(
                                title = "Rest",
                                desc = "Pick how long you want to rest before starting the next focus session",
                                postfix = "min",
                                onSaveClick = { value ->
                                    timerSetupViewModel.setRest(value.minutes)
                                    slot.dismiss()
                                },
                                numberSelectorState = rememberTimerState(
                                    intProgression = 0..60 step 5,
                                    initialValue = state.value.intervalsSettings.rest.inWholeMinutes.toInt()
                                )
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
