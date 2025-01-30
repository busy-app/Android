package com.flipperdevices.bsb.timer.tag.api

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.essenty.backhandler.BackCallback
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.composables.core.SheetDetent
import com.flipperdevices.bsb.core.theme.LocalPallet
import com.flipperdevices.bsb.timer.tag.api.TagSelectionDecomposeComponentImpl.ModalChild.TagsCreate
import com.flipperdevices.bsb.timer.tag.api.TagSelectionDecomposeComponentImpl.ModalChild.TagsList
import com.flipperdevices.bsb.timer.tag.composable.BModalBottomSheetContent
import com.flipperdevices.bsb.timer.tag.composable.TagsSheetContent
import com.flipperdevices.bsb.timer.tag.viewmodel.CreateTagViewModel
import com.flipperdevices.bsb.timer.tag.viewmodel.TagsViewModel
import com.flipperdevices.ui.button.BChipButton
import com.flipperdevices.ui.decompose.ScreenDecomposeComponent
import com.flipperdevices.ui.sheet.BModalBottomSheetContent
import com.flipperdevices.ui.sheet.ModalBottomSheetSlot
import kotlinx.serialization.Serializable
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class TagSelectionDecomposeComponentImpl(
    @Assisted componentContext: ComponentContext,
    private val tagsViewModelFactory: () -> TagsViewModel,
    private val createTagViewModelFactory: () -> CreateTagViewModel
) : ScreenDecomposeComponent(componentContext) {
    @Serializable
    private sealed interface ModalConfiguration {
        @Serializable
        data object TagSelector : ModalConfiguration

        @Serializable
        data object CreateTag : ModalConfiguration
    }

    private sealed interface ModalChild {
        class TagsList(val tagsViewModel: TagsViewModel) : ModalChild
        class TagsCreate(val createTagViewModel: CreateTagViewModel) : ModalChild
    }

    private val slotNavigation = SlotNavigation<ModalConfiguration>()
    private val slot = childSlot(
        source = slotNavigation,
        serializer = ModalConfiguration.serializer(),
        childFactory = { config, ctx ->
            when (config) {
                ModalConfiguration.TagSelector -> TagsList(
                    tagsViewModel = ctx.instanceKeeper.getOrCreate { tagsViewModelFactory.invoke() }
                )

                ModalConfiguration.CreateTag -> TagsCreate(
                    createTagViewModel = ctx.instanceKeeper.getOrCreate { createTagViewModelFactory.invoke() }
                )
            }
        }
    )

    init {
        backHandler.register(BackCallback { slotNavigation.dismiss() })
    }

    fun openTags() {
        slotNavigation.activate(ModalConfiguration.TagSelector)
    }

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    override fun Render(modifier: Modifier) {
        ModalBottomSheetSlot(
            initialDetent = SheetDetent.FullyExpanded,
            slot = slot.subscribeAsState().value,
            onDismiss = { slotNavigation.dismiss() },
            content = { child ->
                when (child) {
                    is TagsList -> {
                        val tagsViewModel = child.tagsViewModel
                        TagsSheetContent(
                            tagsViewModel = tagsViewModel,
                            onCreateTag = { slotNavigation.activate(ModalConfiguration.CreateTag) }
                        )
                    }

                    is TagsCreate -> {
                        val createTagViewModel = child.createTagViewModel
                        val state = createTagViewModel.state.collectAsState()
                        BModalBottomSheetContent(
                            background = Color(0xFF383838).copy(alpha = 0.78f)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                TextField(
                                    modifier = Modifier.weight(1f),
                                    value = state.value.tag,
                                    onValueChange = createTagViewModel::onTagChanged
                                )
                                BChipButton(
                                    onClick = {
                                        createTagViewModel.onFinish()
                                        slotNavigation.activate(ModalConfiguration.TagSelector)
                                    },
                                    text = "Save",
                                    painter = null,
                                    fontSize = 18.sp,
                                    contentPadding = PaddingValues(
                                        horizontal = 32.dp,
                                        vertical = 24.dp
                                    ),
                                    background = LocalPallet.current
                                        .accent
                                        .device
                                        .primary
                                )
                            }
                            Spacer(Modifier.navigationBarsPadding())
                        }
                    }
                }
            }
        )
    }
}
