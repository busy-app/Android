package com.flipperdevices.bsb.timer.cards.composable

import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import com.flipperdevices.bsb.timer.cards.model.LocalVideoLayoutInfo

@Composable
internal fun EditCardNameComposable(
    name: String,
    onFinish: () -> Unit,
    onNameChange: (String) -> Unit
) {
    var textFieldValueState by remember {
        mutableStateOf(
            TextFieldValue(
                text = name,
                selection = when {
                    name.isEmpty() -> TextRange.Zero
                    else -> TextRange(name.length, name.length)
                }
            )
        )
    }
    val textFieldValue = textFieldValueState.copy(text = name)
    var lastTextValue by remember(name) { mutableStateOf(name) }

    SideEffect {
        if (textFieldValue.selection != textFieldValueState.selection ||
            textFieldValue.composition != textFieldValueState.composition
        ) {
            textFieldValueState = textFieldValue
        }
    }

    val localDensity: Density = LocalDensity.current
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val localVideoLayoutInfo = LocalVideoLayoutInfo.current
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    CompositionLocalProvider(LocalIndication provides object : Indication {}) {
        Box(Modifier.fillMaxSize().clickable(onClick = onFinish))
    }
    LaunchedEffect(focusRequester) {
        if (!focusRequester.captureFocus()) {
            onFinish.invoke()
        }
    }
    Column(
        modifier = Modifier.fillMaxSize()
            .systemBarsPadding()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = textFieldValue,
            colors = TextFieldDefaults.textFieldColors(
                textColor = LocalCorruptedPallet.current.white.invert,
                backgroundColor = Color.Transparent,
                cursorColor = LocalCorruptedPallet.current.white.invert,
                focusedIndicatorColor = Color.Transparent,
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, showKeyboardOnFocus = true),
            keyboardActions = KeyboardActions(onDone = {
                focusRequester.freeFocus()
                keyboardController?.hide()
                onFinish.invoke()
            }),
            onValueChange = { newTextFieldValueState ->
                textFieldValueState = newTextFieldValueState

                val stringChangedSinceLastInvocation = lastTextValue != newTextFieldValueState.text
                lastTextValue = newTextFieldValueState.text

                if (stringChangedSinceLastInvocation) {
                    onNameChange.invoke(newTextFieldValueState.text)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .padding(top = 86.dp)
                .padding(16.dp)
                .border(2.dp, LocalCorruptedPallet.current.neutral.primary, RoundedCornerShape(16.dp))
                .onGloballyPositioned {
                    localVideoLayoutInfo.clearVideoTopOffsets()
                    localVideoLayoutInfo.addVideoTopOffset(
                        key = "title_edit_text_height",
                        offset = with(localDensity) { it.size.height.toDp() }
                    )
                    localVideoLayoutInfo.addVideoTopOffset(
                        key = "title_edit_text_offset_y",
                        offset = with(localDensity) { it.positionInParent().y.toDp() }
                    )
                }
        )
    }
}
