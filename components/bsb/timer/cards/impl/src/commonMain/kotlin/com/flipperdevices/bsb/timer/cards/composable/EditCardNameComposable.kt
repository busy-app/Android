package com.flipperdevices.bsb.timer.cards.composable

import androidx.compose.animation.Animatable
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.zIndex
import com.flipperdevices.bsb.core.theme.LocalCorruptedPallet
import com.flipperdevices.bsb.timer.cards.api.LocalAnimatedVisibilityScope
import com.flipperdevices.bsb.timer.cards.api.LocalSharedTransitionScope
import com.flipperdevices.bsb.timer.cards.model.LocalVideoLayoutInfo
import kotlinx.coroutines.launch

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
internal fun EditCardNameComposable(
    name: String,
    onFinish: () -> Unit,
    onNameChange: (String) -> Unit,
    modifier: Modifier = Modifier
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
    val scope = rememberCoroutineScope()
    val animatedBorderColor = remember { Animatable(Color.Transparent) }
    val targetBorderColor = LocalCorruptedPallet.current.neutral.primary
    LaunchedEffect(Unit) {
        animatedBorderColor.animateTo(targetBorderColor,tween(1000))
    }
    Column(
        modifier = modifier.fillMaxWidth()
            .systemBarsPadding()
            .navigationBarsPadding()
            .zIndex(1f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        with(LocalSharedTransitionScope.current) {
            Box(
                Modifier
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
                        .border(
                            2.dp,
                            animatedBorderColor.value,
                            RoundedCornerShape(16.dp)
                        )
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
                                    "title_edit_text_height" to with(localDensity) { it.size.height.toDp() },
                                    "title_edit_text_offset_y" to with(localDensity) { it.positionInParent().y.toDp() }
                                )
                            }
                        }
                )
            }
        }
    }
}
