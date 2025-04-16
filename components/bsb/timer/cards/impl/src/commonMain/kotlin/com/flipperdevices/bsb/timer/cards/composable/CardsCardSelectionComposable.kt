package com.flipperdevices.bsb.timer.cards.composable

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import com.flipperdevices.bsb.timer.cards.model.LayoutOffsetData
import com.flipperdevices.bsb.timer.cards.model.PagerData
import com.flipperdevices.bsb.timer.common.composable.appbar.ButtonTimerComposable
import com.flipperdevices.bsb.timer.common.composable.appbar.ButtonTimerState
import kotlin.math.absoluteValue
import kotlin.math.sign

@Composable
internal fun CardsCardSelectionComposable(
    layoutOffsetDataState: MutableState<LayoutOffsetData>,
    currentData: PagerData,
    pagerState: PagerState,
    onNameClick: () -> Unit,
    onOptionsClick: () -> Unit
) {
    val pagerItemAlpha by animateFloatAsState(
        targetValue = pagerState.currentPageOffsetFraction
            .times(pagerState.currentPageOffsetFraction.sign)
            .times(2)
            .minus(1f)
            .absoluteValue,
    )
    val localDensity: Density = LocalDensity.current
    Column(
        modifier = Modifier.fillMaxSize()
            .systemBarsPadding()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .padding(16.dp)
                .padding(top = 86.dp)
                .clickable(onClick = onNameClick)
                .onGloballyPositioned {
                    layoutOffsetDataState.value = layoutOffsetDataState.value.copy(
                        titleHeightDp = with(localDensity) { it.size.height.toDp() },
                        titleOffsetYDp = with(localDensity) { it.positionInParent().y.toDp() }
                    )
                },
            text = currentData.timerSettings.name,
            color = LocalCorruptedPallet.current
                .white
                .invert
                .withCoercedAtMostAlpha(alpha = pagerItemAlpha),
            fontSize = 22.sp,
            fontFamily = LocalBusyBarFonts.current.jetbrainsMono,
            fontWeight = FontWeight.W500
        )
        Spacer(Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .height(layoutOffsetDataState.value.videoHeightDp)
                .fillMaxWidth()
        )
        PagerDescComposable(
            timerSettings = currentData.timerSettings,
            alpha = pagerItemAlpha
        )
        Spacer(Modifier.height(40.dp))
        ButtonTimerComposable(
            state = ButtonTimerState.START,
            onClick = {}
        )
        Spacer(Modifier.height(40.dp))
        OptionsComposable(onClick = onOptionsClick)
        Box(Modifier.weight(1f))
        WormDotPagerIndicator(
            count = pagerState.pageCount,
            pagerState = pagerState
        )
    }
}
