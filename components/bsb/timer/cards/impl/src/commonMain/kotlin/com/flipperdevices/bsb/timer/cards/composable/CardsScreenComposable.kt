package com.flipperdevices.bsb.timer.cards.composable

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import busystatusbar.components.bsb.timer.cards.impl.generated.resources.Res
import busystatusbar.components.bsb.timer.cards.impl.generated.resources.ic_three_dots
import com.flipperdevices.bsb.core.theme.LocalBusyBarFonts
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import com.flipperdevices.bsb.timer.cards.model.EXAMPLE_DATA
import com.flipperdevices.bsb.timer.cards.model.LayoutData
import com.flipperdevices.bsb.timer.cards.model.asLayoutData
import com.flipperdevices.bsb.timer.common.composable.appbar.ButtonTimerComposable
import com.flipperdevices.bsb.timer.common.composable.appbar.ButtonTimerState
import com.flipperdevices.ui.video.BSBVideoPlayer
import kotlin.math.sign
import org.jetbrains.compose.resources.ExperimentalResourceApi

private const val SQUARE_RATIO = 1f / 1f


@OptIn(ExperimentalResourceApi::class)
@Composable
fun CardsScreenComposable(modifier: Modifier = Modifier) {
    val items = EXAMPLE_DATA
    var videoLayoutData by remember { mutableStateOf(LayoutData.Zero) }
    var titleLayoutData by remember { mutableStateOf(LayoutData.Zero) }
    val localDensity: Density = LocalDensity.current

    val pagerState = rememberPagerState {
        items.size
    }
    val pagerItemAlpha by animateFloatAsState(
        targetValue = 1f - pagerState.currentPageOffsetFraction.times(pagerState.currentPageOffsetFraction.sign) * 2,
    )
    HorizontalPager(state = pagerState) { page ->
        val data = items[page]
        Box(
            modifier = Modifier
                .systemBarsPadding()
                .padding(top = titleLayoutData.offset.y),
            contentAlignment = Alignment.TopCenter
        ) {
            BSBVideoPlayer(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .aspectRatio(SQUARE_RATIO)
                    .onGloballyPositioned { videoLayoutData = it.asLayoutData(localDensity) },
                uri = data.videoUri,
                firstFrame = Res.drawable.ic_three_dots // todo
            )
        }
    }

    val currentData = items[pagerState.currentPage]
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
                .onGloballyPositioned { titleLayoutData = it.asLayoutData(localDensity) },
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
                .height(videoLayoutData.size.height.plus(titleLayoutData.size.height))
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
        OptionsComposable()
        Box(Modifier.weight(1f))
        WormDotPagerIndicator(
            count = pagerState.pageCount,
            pagerState = pagerState
        )
    }
}
