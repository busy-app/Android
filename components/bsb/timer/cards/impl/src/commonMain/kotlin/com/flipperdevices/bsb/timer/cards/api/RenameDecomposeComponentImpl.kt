package com.flipperdevices.bsb.timer.cards.api

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import busystatusbar.components.bsb.timer.cards.impl.generated.resources.Res
import busystatusbar.components.bsb.timer.cards.impl.generated.resources.ic_three_dots
import busystatusbar.components.bsb.timer.cards.impl.generated.resources.img_dots_first_frame
import busystatusbar.components.bsb.timer.cards.impl.generated.resources.img_particles_first_frame
import com.arkivanov.decompose.ComponentContext
import com.flipperdevices.bsb.timer.cards.composable.EditCardNameComposable
import com.flipperdevices.bsb.timer.cards.composable.SQUARE_RATIO
import com.flipperdevices.bsb.timer.cards.model.LocalVideoLayoutInfo
import com.flipperdevices.ui.decompose.DecomposeOnBackParameter
import com.flipperdevices.ui.decompose.ScreenDecomposeComponent
import com.flipperdevices.ui.video.BSBVideoPlayer
import kotlin.math.absoluteValue
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class RenameDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    @Assisted private val onBack: DecomposeOnBackParameter,
    @Assisted private val cardRenameItem: CardRenameItem
) : ScreenDecomposeComponent(componentContext) {

    @OptIn(ExperimentalSharedTransitionApi::class)
    @Composable
    override fun Render(modifier: Modifier) {
        val density = LocalDensity.current
        val videoLayoutInfo = LocalVideoLayoutInfo.current

        Box(
            Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectVerticalDragGestures { change, offset ->
                        println(offset)
                        if (offset.absoluteValue > 10) {

                        }
                    }
                }
        ) {
            with(LocalSharedTransitionScope.current) {
                BSBVideoPlayer(
                    modifier = Modifier
                        .zIndex(-1f)
                        .fillMaxWidth()
                        .systemBarsPadding()
                        .padding(top = animateDpAsState(LocalVideoLayoutInfo.current.videoTopOffsetDp).value)
                        .padding(horizontal = 32.dp)
                        .aspectRatio(SQUARE_RATIO)
                        .sharedBounds(
                            sharedContentState = rememberSharedContentState(key = "video"),
                            animatedVisibilityScope = LocalAnimatedVisibilityScope.current,
                            enter = fadeIn(),
                            exit = fadeOut(),
                            resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds()
                        )
                        .onGloballyPositioned { layoutCoordinates ->
                            val heightDp = with(density) { layoutCoordinates.size.height.toDp() }
                            videoLayoutInfo.setVideoHeightDp(heightDp)
                        },
                    uri = cardRenameItem.videoUri,
                    firstFrame = Res.drawable.img_dots_first_frame // todo
                )
            }

            EditCardNameComposable(
                name = cardRenameItem.name,
                onFinish = {
                    onBack.invoke()
                },
                onNameChange = {
                },
                modifier = Modifier
            )
        }
    }
}