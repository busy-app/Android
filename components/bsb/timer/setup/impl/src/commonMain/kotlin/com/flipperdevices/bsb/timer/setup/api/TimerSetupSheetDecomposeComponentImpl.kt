package com.flipperdevices.bsb.timer.setup.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.flipperdevices.bsb.appblocker.card.api.AppBlockerCardContentDecomposeComponent
import com.flipperdevices.bsb.dao.model.TimerSettingsId
import com.flipperdevices.bsb.timer.setup.composable.timer.TimerSetupModalBottomSheetContent
import com.flipperdevices.bsb.timer.setup.model.CardEditScreenState
import com.flipperdevices.bsb.timer.setup.viewmodel.TimerSetupViewModel
import com.flipperdevices.core.ui.lifecycle.viewModelWithFactory
import com.flipperdevices.ui.decompose.DecomposeOnBackParameter
import com.flipperdevices.ui.decompose.ModalDecomposeComponent
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class TimerSetupSheetDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    @Assisted private val timerSettingsId: TimerSettingsId,
    @Assisted private val onBack: DecomposeOnBackParameter,
    intervalsSetupSheetDecomposeComponentFactory: IntervalsSetupSheetDecomposeComponent.Factory,
    timerSetupViewModelFactory: (TimerSettingsId) -> TimerSetupViewModel,
    appBlockerCardContentFactory: AppBlockerCardContentDecomposeComponent.Factory,
) : ModalDecomposeComponent(componentContext) {
    private val intervalsSetupSheetDecomposeComponent =
        intervalsSetupSheetDecomposeComponentFactory(
            childContext("intervalsSetupSheetDecomposeComponent_intervalsSheet"),
            timerSettingsId
        )

    private val timerSetupViewModel = viewModelWithFactory(timerSettingsId) {
        timerSetupViewModelFactory(timerSettingsId)
    }
    private val appBlockerCardContent = appBlockerCardContentFactory(
        componentContext = childContext("intervalsSetupSheetDecomposeComponent_appBlockerCardContent"),
        timerSettingsId = timerSettingsId,
        onBackParameter = { }
    )

    @Composable
    override fun Render(modifier: Modifier) {
        val state by timerSetupViewModel.getState().collectAsState()
        val timerSettings = when (val localState = state) {
            CardEditScreenState.NotInitialized -> return
            is CardEditScreenState.Loaded -> if (localState.timerSettings == null) {
                onBack()
                return
            } else {
                localState.timerSettings
            }
        }

        TimerSetupModalBottomSheetContent(
            timerSettings = timerSettings,
            onTotalTimeChange = { duration ->
                timerSetupViewModel.setTotalTime(timerSettings, duration)
            },
            onSaveClick = {
                onBack()
            },
            onIntervalsToggle = {
                timerSetupViewModel.toggleIntervals(timerSettings)
            },
            onShowLongRestTimer = {
                intervalsSetupSheetDecomposeComponent.showLongRest()
            },
            onShowWorkTimer = {
                intervalsSetupSheetDecomposeComponent.showWork()
            },
            onShowRestTimer = {
                intervalsSetupSheetDecomposeComponent.showRest()
            },
            appBlockerCardContent = {
                appBlockerCardContent.Render(Modifier)
            },
            onSoundToggle = timerSetupViewModel::onSoundToggle
        )
        intervalsSetupSheetDecomposeComponent.Render(Modifier)
    }
}
