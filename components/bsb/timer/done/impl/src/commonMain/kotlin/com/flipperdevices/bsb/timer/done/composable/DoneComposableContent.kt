package com.flipperdevices.bsb.timer.done.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import busystatusbar.components.bsb.timer.done.impl.generated.resources.Res
import busystatusbar.components.bsb.timer.done.impl.generated.resources.ic_pomodoro
import busystatusbar.components.bsb.timer.done.impl.generated.resources.ic_star
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalPallet
import com.flipperdevices.ui.button.BChipButton
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.DurationUnit

@Composable
private fun RoadmapComposableItem(
    text: String,
    duration: Duration,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_pomodoro),
            tint = Color.Unspecified,
            modifier = Modifier.size(18.dp),
            contentDescription = null
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = text,
                color = LocalPallet.current
                    .white
                    .invert,
                fontSize = 18.sp,
                fontWeight = FontWeight.W500
            )
            Text(
                text = duration.toString(DurationUnit.MINUTES),
                color = LocalPallet.current
                    .white
                    .invert,
                fontSize = 18.sp,
                fontWeight = FontWeight.W400
            )
        }
    }
}

// todo This method will be rewritten with another design
@Suppress("LongMethod")
@Composable
fun DoneComposableContent(
    modifier: Modifier = Modifier,
    onOkClick: () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(color = 0xFF27486C), // todo
                            Color(color = 0xFF0E1E48), // todo
                        )
                    )
                )
        )
        Column(
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_star),
                    tint = Color.Unspecified,
                    modifier = Modifier.size(64.dp),
                    contentDescription = null
                )
                Text(
                    text = "Well done!",
                    fontWeight = FontWeight.W500,
                    color = LocalPallet.current
                        .white
                        .invert,
                    fontSize = 32.sp
                )
            }

            Spacer(Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = LocalPallet.current
                            .transparent
                            .whiteInvert
                            .quaternary
                            .copy(alpha = 0.1f)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = "1h 15min",
                    color = LocalPallet.current
                        .white
                        .invert,
                    fontSize = 18.sp
                )
                Text(
                    text = "Total today’s focus time",
                    color = LocalPallet.current
                        .transparent
                        .whiteInvert
                        .secondary
                        .copy(alpha = 0.3f),
                    fontSize = 16.sp
                )
                Spacer(Modifier.height(26.dp))
                Text(
                    text = "3x",
                    color = LocalPallet.current
                        .white
                        .invert,
                    fontSize = 18.sp
                )
                Text(
                    text = "Times saved from distractions",
                    color = LocalPallet.current
                        .transparent
                        .whiteInvert
                        .secondary
                        .copy(alpha = 0.3f),
                    fontSize = 16.sp
                )
            }
            Spacer(Modifier.height(48.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Text(
                    text = "Time you spend:",
                    color = LocalPallet.current
                        .transparent
                        .whiteInvert
                        .secondary
                        .copy(alpha = 0.3f),
                    fontSize = 16.sp
                )
                RoadmapComposableItem(
                    text = "Finish the roadmap for mobile app",
                    duration = 45.minutes
                )
                RoadmapComposableItem(
                    text = "Finish the roadmap for mobile app",
                    duration = 45.minutes.plus(1.hours)
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .align(Alignment.BottomCenter),
            contentAlignment = Alignment.Center
        ) {
            BChipButton(
                onClick = onOkClick,
                text = "Ok",
                fontSize = 18.sp,
                painter = null,
                background = LocalPallet.current
                    .transparent
                    .whiteInvert
                    .tertiary
                    .copy(alpha = 0.1f),
                contentPadding = PaddingValues(
                    horizontal = 64.dp,
                    vertical = 16.dp
                ),
                contentColor = LocalPallet.current
                    .transparent
                    .whiteInvert
                    .primary
                    .copy(alpha = 0.5f)
            )
        }
    }
}

@Preview
@Composable
private fun DoneComposableContentPreview() {
    BusyBarThemeInternal {
        DoneComposableContent(
            onOkClick = {}
        )
    }
}
