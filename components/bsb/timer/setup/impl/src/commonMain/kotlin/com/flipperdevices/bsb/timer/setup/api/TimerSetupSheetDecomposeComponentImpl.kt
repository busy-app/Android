package com.flipperdevices.bsb.timer.setup.api

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.composables.core.SheetDetent
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import com.flipperdevices.bsb.core.theme.LocalPallet
import com.flipperdevices.bsb.timer.setup.viewmodel.TimerSetupViewModel
import com.flipperdevices.core.di.AppGraph
import com.flipperdevices.ui.picker.NumberSelectorComposable
import com.flipperdevices.ui.picker.rememberTimerState
import com.flipperdevices.ui.sheet.BModalBottomSheetContent
import com.flipperdevices.ui.sheet.ModalBottomSheetSlot
import kotlinx.serialization.builtins.serializer
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import kotlin.time.Duration.Companion.minutes

@Inject
class TimerSetupSheetDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    intervalsSetupSheetDecomposeComponentFactory: IntervalsSetupSheetDecomposeComponent.Factory,
    timerSetupViewModelFactory: () -> TimerSetupViewModel
) : TimerSetupSheetDecomposeComponent(componentContext) {
    private val timerSetupViewModel = instanceKeeper.getOrCreate {
        timerSetupViewModelFactory.invoke()
    }
    private val intervalsSetupSheetDecomposeComponent = intervalsSetupSheetDecomposeComponentFactory(
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
    fun IntervalComposable(
        text: String,
        value: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .clickable { onClick.invoke() }
        ) {
            Text(
                text = text,
                color = LocalPallet.current
                    .transparent
                    .whiteInvert
                    .primary
                    .copy(alpha = 0.5f),
                fontSize = 18.sp,
                fontWeight = FontWeight.W500
            )
            Box(
                modifier = Modifier
                    .width(74.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onClick.invoke() }
                    .background(
                        color = LocalPallet.current
                            .transparent
                            .whiteInvert
                            .tertiary
                            .copy(alpha = 0.1f)
                    )
                    .padding(horizontal = 12.dp)
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = value,
                    color = LocalPallet.current
                        .white
                        .invert,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.W500,
                    fontFamily = LocalBusyBarFonts.current.jetbrainsMono
                )
            }
        }
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
                BModalBottomSheetContent {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(32.dp),
                        horizontalAlignment = Alignment.Start,
                        modifier = modifier.fillMaxWidth().padding(24.dp).navigationBarsPadding()
                    ) {
                        Text(
                            text = "Timer",
                            fontSize = 32.sp,
                            color = LocalPallet.current
                                .white
                                .invert,
                            fontWeight = FontWeight.W500
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(224.dp)
                                .clip(RoundedCornerShape(32.dp))
                                .background(Color(color = 0xFF2E2E2E)), // todo
                            contentAlignment = Alignment.Center
                        ) {
                            NumberSelectorComposable(
                                modifier = Modifier,
                                numberSelectorState = rememberTimerState(
                                    intProgression = 0..60 step 5,
                                    initialValue = state.value.timer.inWholeMinutes.toInt()
                                ),
                                postfix = "min",
                                onValueChange = { value ->
                                    timerSetupViewModel.setTimer(value.minutes)
                                }
                            )
                        }
                        Column(
                            verticalArrangement = Arrangement.spacedBy(24.dp),
                            horizontalAlignment = Alignment.Start,
                        ) {
                            IntervalComposable(
                                text = "Rest, min",
                                value = "${state.value.intervalsSettings.rest.inWholeMinutes}",
                                onClick = { intervalsSetupSheetDecomposeComponent.showRest() }
                            )
                            IntervalComposable(
                                text = "Long rest, min",
                                value = "${state.value.intervalsSettings.longRest.inWholeMinutes}",
                                onClick = { intervalsSetupSheetDecomposeComponent.showLongRest() }
                            )
                            IntervalComposable(
                                text = "Cycles",
                                value = "${state.value.intervalsSettings.cycles}",
                                onClick = { intervalsSetupSheetDecomposeComponent.showCycles() }
                            )
                        }
                    }
                }
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
