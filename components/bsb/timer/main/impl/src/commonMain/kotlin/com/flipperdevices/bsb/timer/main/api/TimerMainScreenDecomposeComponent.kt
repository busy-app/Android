package com.flipperdevices.bsb.timer.main.api

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.pushNew
import com.flipperdevices.bsb.core.theme.LocalPallet
import com.flipperdevices.bsb.preference.api.PreferenceApi
import com.flipperdevices.bsb.preference.api.ThemeStatusBarIconStyleProvider
import com.flipperdevices.bsb.timer.main.composable.BusyButtonComposable
import com.flipperdevices.bsb.timer.main.composable.StartButtonComposable
import com.flipperdevices.bsb.timer.main.model.TimerMainNavigationConfig
import com.flipperdevices.bsb.timer.setup.api.TimerSetupScreenDecomposeComponent
import com.flipperdevices.ui.decompose.ScreenDecomposeComponent
import com.flipperdevices.ui.decompose.statusbar.StatusBarIconStyleProvider
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class TimerMainScreenDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    @Assisted private val navigation: StackNavigation<TimerMainNavigationConfig>,
    iconStyleProvider: ThemeStatusBarIconStyleProvider,
    private val timerSetupDecomposeComponentFactory: TimerSetupScreenDecomposeComponent.Factory
) : ScreenDecomposeComponent(componentContext), StatusBarIconStyleProvider by iconStyleProvider {
    private val setupDecomposeComponent = timerSetupDecomposeComponentFactory(
        componentContext = childContext("setupTimerDecomposeComponent")
    )

    @Composable
    override fun Render(modifier: Modifier) {
        Column(
            modifier
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .clip(
                        RoundedCornerShape(
                            topStart = 0.dp,
                            topEnd = 0.dp,
                            bottomEnd = 16.dp,
                            bottomStart = 16.dp
                        )
                    )
                    .background(LocalPallet.current.surface.primary)
                    .statusBarsPadding()
                    .padding(top = 16.dp)
            )
            Column(
                Modifier
                    .padding(top = 2.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomEnd = 0.dp,
                            bottomStart = 0.dp
                        )
                    )
                    .background(LocalPallet.current.surface.primary)
            ) {
                setupDecomposeComponent.Render(
                    Modifier
                        .weight(1f)
                )
                val state by setupDecomposeComponent.timerState.collectAsState()
                StartButtonComposable(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 16.dp,
                            bottom = 128.dp,
                            start = 16.dp,
                            end = 16.dp
                        ),
                    onClick = {
                        navigation.pushNew(TimerMainNavigationConfig.Timer(state))
                    }
                )
            }
        }
    }
}