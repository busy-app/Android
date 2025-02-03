package com.flipperdevices.bsb.timer.setup.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalPallet
import com.flipperdevices.ui.button.BChipButton
import com.flipperdevices.ui.picker.NumberSelectorComposable
import com.flipperdevices.ui.picker.NumberSelectorState
import com.flipperdevices.ui.picker.rememberTimerState
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PickerContent(
    title: String,
    desc: String,
    postfix: String? = null,
    numberSelectorState: NumberSelectorState,
    modifier: Modifier = Modifier,
    onSaveClick: (Int) -> Unit
) {
    var value by remember(numberSelectorState) {
        mutableStateOf(numberSelectorState.initialValue)
    }
    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        horizontalAlignment = Alignment.Start,
        modifier = modifier.fillMaxWidth().padding(24.dp).navigationBarsPadding()
    ) {
        Text(
            text = title,
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
                .background(Color(0xFF2E2E2E)),
            contentAlignment = Alignment.Center
        ) {
            NumberSelectorComposable(
                modifier = Modifier,
                numberSelectorState = numberSelectorState,
                postfix = postfix,
                onValueChanged = { newValue ->
                    value = newValue
                }
            )
        }

        Text(
            text = desc,
            fontSize = 16.sp,
            color = LocalPallet.current
                .neutral
                .tertiary,
            fontWeight = FontWeight.W400
        )

        BChipButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                onSaveClick.invoke(value)
            },
            painter = null,
            text = "Save",
            contentColor = LocalPallet.current
                .white
                .invert,
            background = LocalPallet.current
                .transparent
                .whiteInvert
                .tertiary
                .copy(alpha = 0.1f)
        )
    }
}

@Composable
@Preview
private fun PickerContentPreview() {
    BusyBarThemeInternal {
        PickerContent(
            title = "Long rest",
            desc = "Pick how long you want to relax after completing several cycles",
            onSaveClick = {},
            numberSelectorState = rememberTimerState(
                0..60 step 5,
            )
        )
    }
}
