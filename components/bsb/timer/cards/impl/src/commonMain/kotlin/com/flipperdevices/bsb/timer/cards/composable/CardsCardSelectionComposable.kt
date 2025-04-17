package com.flipperdevices.bsb.timer.cards.composable

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import com.flipperdevices.bsb.timer.cards.api.LocalAnimatedVisibilityScope
import com.flipperdevices.bsb.timer.cards.api.LocalSharedTransitionScope
import com.flipperdevices.bsb.timer.cards.model.LocalVideoLayoutInfo
import com.flipperdevices.bsb.timer.cards.model.PagerData
import com.flipperdevices.bsb.timer.common.composable.appbar.ButtonTimerComposable
import com.flipperdevices.bsb.timer.common.composable.appbar.ButtonTimerState
import kotlin.math.absoluteValue
import kotlin.math.sign
import kotlinx.coroutines.launch

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
internal fun CardsCardSelectionComposable(
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
    val localVideoLayoutInfo = LocalVideoLayoutInfo.current
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier.fillMaxSize()
            .systemBarsPadding()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        with(LocalSharedTransitionScope.current) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .padding(top = 86.dp)
                    .clickable(onClick = onNameClick)
                    .sharedBounds(
                        rememberSharedContentState(key = "title"),
                        animatedVisibilityScope = LocalAnimatedVisibilityScope.current,
                        enter = fadeIn(),
                        exit = fadeOut(),
                        resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds()
                    )
                    .onGloballyPositioned {
                        scope.launch {
                            localVideoLayoutInfo.setVideoTopOffset(
                                "title_height" to with(localDensity) { it.size.height.toDp() },
                                "title_offset_y" to with(localDensity) { it.positionInParent().y.toDp() }
                            )
                        }
                    },
                text = currentData.timerSettings.name,
                textAlign = TextAlign.Center,
                color = LocalCorruptedPallet.current
                    .white
                    .invert
                    .withCoercedAtMostAlpha(alpha = pagerItemAlpha),
                fontSize = 22.sp,
                fontFamily = LocalBusyBarFonts.current.jetbrainsMono,
                fontWeight = FontWeight.W500
            )
        }
        Spacer(Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .height(LocalVideoLayoutInfo.current.videoHeightDp)
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
