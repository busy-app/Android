package com.flipperdevices.bsb.timer.done.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import busystatusbar.components.bsb.timer.done.impl.generated.resources.Res
import busystatusbar.components.bsb.timer.done.impl.generated.resources.ic_pomodoro
import busystatusbar.components.bsb.timer.done.impl.generated.resources.td_finish
import busystatusbar.components.bsb.timer.done.impl.generated.resources.td_restart
import busystatusbar.components.bsb.timer.done.impl.generated.resources.td_well_done
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import com.flipperdevices.ui.button.BChipButton
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

private data class ObjectiveModel(
    val title: String,
    val innerDesc: String
)

@Suppress("LongMethod")
@Composable
private fun ObjectiveCard(
    objectives: ImmutableList<ObjectiveModel>,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(LocalCorruptedPallet.current.transparent.whiteInvert.quaternary)
            .height(IntrinsicSize.Max)
            .padding(16.dp)
    ) {
        objectives.forEachIndexed { i, objective ->
            val isLast = i == objectives.lastIndex
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_pomodoro),
                    tint = Color.Unspecified,
                    modifier = Modifier.size(40.dp),
                    contentDescription = null
                )
                Text(
                    text = objective.title,
                    color = LocalCorruptedPallet.current
                        .white
                        .invert,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(48.dp))
                        .background(LocalCorruptedPallet.current.transparent.whiteInvert.tertiary)
                        .padding(horizontal = 8.dp)
                        .padding(vertical = 4.dp)
                        .fillMaxWidth(fraction = 0.4f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = objective.innerDesc,
                        color = LocalCorruptedPallet.current
                            .transparent
                            .whiteInvert
                            .secondary,
                        fontSize = 16.sp,
                    )
                }
            }
            if (!isLast) {
                Box(
                    Modifier
                        .padding(horizontal = 5.dp)
                        .width(1.dp)
                        .fillMaxHeight()
                        .background(Color(color = 0xFFFFFF).copy(alpha = 0.2f)) // todo
                )
            }
        }
    }
}

// todo This method will be rewritten with another design
@Suppress("LongMethod")
@Composable
fun DoneComposableContent(
    onFinishClick: () -> Unit,
    onRestartClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(color = 0xFF000000), // todo
                            Color(color = 0xFF0E1448), // todo
                        )
                    )
                )
        )
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 24.dp)
                .padding(vertical = 54.dp)
                .statusBarsPadding()
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(Res.string.td_well_done),
                    fontWeight = FontWeight.W500,
                    color = LocalCorruptedPallet.current
                        .white
                        .invert,
                    fontSize = 40.sp
                )
                Text(
                    text = stringResource(Res.string.td_finish),
                    fontWeight = FontWeight.W500,
                    color = LocalCorruptedPallet.current
                        .white
                        .invert,
                    fontSize = 18.sp
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ObjectiveCard(
                    objectives = persistentListOf(
                        ObjectiveModel(
                            "You’re total BUSY amount:", // todo raw string
                            "x5" // todo raw string
                        ),
                        ObjectiveModel(
                            "You tried to open blocked apps today:", // todo raw string
                            "x3" // todo raw string
                        )
                    )
                )
                BChipButton(
                    onClick = onRestartClick,
                    text = stringResource(Res.string.td_restart),
                    fontSize = 18.sp,
                    painter = null,
                    background = Color.Transparent,
                    contentPadding = PaddingValues(
                        horizontal = 64.dp,
                        vertical = 16.dp
                    ),
                    contentColor = LocalCorruptedPallet.current
                        .transparent
                        .whiteInvert
                        .primary
                )
            }
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                BChipButton(
                    onClick = onFinishClick,
                    text = stringResource(Res.string.td_finish),
                    fontSize = 18.sp,
                    painter = null,
                    background = LocalCorruptedPallet.current
                        .transparent
                        .whiteInvert
                        .tertiary,
                    contentPadding = PaddingValues(
                        horizontal = 64.dp,
                        vertical = 16.dp
                    ),
                    contentColor = LocalCorruptedPallet.current
                        .white
                        .invert
                )
            }
        }
    }
}

@Preview
@Composable
private fun DoneComposableContentPreview() {
    BusyBarThemeInternal {
        DoneComposableContent(
            onFinishClick = {},
            onRestartClick = {}
        )
    }
}
