package com.flipperdevices.bsb.timer.setup.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.composables.core.SheetDetent
import com.flipperdevices.bsb.appblocker.filter.api.AppBlockerFilterApi
import com.flipperdevices.bsb.appblocker.filter.api.model.BlockedAppCount
import com.flipperdevices.bsb.timer.setup.composable.timer.TimerSetupModalBottomSheetContent
import com.flipperdevices.bsb.timer.setup.viewmodel.TimerSetupViewModel
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.ui.sheet.BModalBottomSheetContent
import com.flipperdevices.ui.sheet.ModalBottomSheetSlot
import kotlinx.serialization.builtins.serializer
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
class TimerSetupSheetDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    intervalsSetupSheetDecomposeComponentFactory: IntervalsSetupSheetDecomposeComponent.Factory,
    timerSetupViewModelFactory: () -> TimerSetupViewModel,
    private val appBlockerFilterApi: AppBlockerFilterApi
) : TimerSetupSheetDecomposeComponent(componentContext) {
    private val timerSetupViewModel = instanceKeeper.getOrCreate {
        timerSetupViewModelFactory.invoke()
    }
    private val intervalsSetupSheetDecomposeComponent =
        intervalsSetupSheetDecomposeComponentFactory(
            componentContext = childContext("restSheetDecomposeComponent")
        )
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

    fun dismiss() {
        slot.dismiss()
    }

    @Composable
    override fun Render(modifier: Modifier) {
        val timerSettings = timerSetupViewModel.state.collectAsState()
        ModalBottomSheetSlot(
            slot = childSlot,
            initialDetent = SheetDetent.FullyExpanded,
            onDismiss = { slot.dismiss() },
            content = {
                BModalBottomSheetContent(
                    horizontalPadding = 0.dp,
                    content = {
                        val appBlockerState by remember {
                            appBlockerFilterApi.getBlockedAppCount()
                        }.collectAsState(BlockedAppCount.TurnOff)

                        TimerSetupModalBottomSheetContent(
                            timerSettings = timerSettings.value,
                            onTotalTimeChange = { duration ->
                                timerSetupViewModel.setTotalTime(duration)
                            },
                            onSaveClick = {
                                slot.dismiss()
                            },
                            onIntervalsToggle = {
                                timerSetupViewModel.toggleIntervals()
                            },
                            onShowLongRestTimer = {
                                intervalsSetupSheetDecomposeComponent.showLongRest()
                            },
                            onSoundClick = {
                                intervalsSetupSheetDecomposeComponent.showSound()
                            },
                            appBlockerState = appBlockerState,
                            onBlockedAppsClick = {
                                intervalsSetupSheetDecomposeComponent.showBlockedApps()
                            },
                            onShowWorkTimer = {
                                intervalsSetupSheetDecomposeComponent.showWork()
                            },
                            onShowRestTimer = {
                                intervalsSetupSheetDecomposeComponent.showRest()
                            }
                        )
                    }
                )
            }
        )
        intervalsSetupSheetDecomposeComponent.Render(Modifier)
    }

    @Inject
    @ContributesBinding(AppGraph::class, TimerSetupSheetDecomposeComponent.Factory::class)
    class Factory(
        private val factory: (
            componentContext: ComponentContext
        ) -> TimerSetupSheetDecomposeComponentImpl
    ) : TimerSetupSheetDecomposeComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext
        ) = factory(componentContext)
    }
}
