package com.flipperdevices.bsb.timer.tag.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.core.ModalBottomSheet
import com.composables.core.ModalBottomSheetScope
import com.composables.core.Scrim
import com.composables.core.SheetDetent
import com.composables.core.rememberModalBottomSheetState
import com.flipperdevices.bsb.core.theme.BusyBarThemeInternal
import com.flipperdevices.bsb.core.theme.LocalPallet
import com.flipperdevices.bsb.timer.tag.viewmodel.TagsViewModel
import com.flipperdevices.ui.button.BChipButton
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun ModalBottomSheetScope.TagsSheetContent(
    tagsViewModel: TagsViewModel,
    onCreateTag: () -> Unit
) {
    val state = tagsViewModel.state.collectAsState()

    TagsSheetContent(
        state = state.value,
        onCreateTag = onCreateTag,
        onSelect = { tags -> tagsViewModel.select(tags) }
    )
}

@Composable
internal fun ModalBottomSheetScope.TagsSheetContent(
    state: TagsViewModel.State,
    onCreateTag: () -> Unit,
    onSelect: (String) -> Unit
) {
    Scrim()
    BModalBottomSheetContent {
        Column {
            Text(
                text = "Select tag",
                color = LocalPallet.current
                    .white
                    .invert,
                fontSize = 32.sp,
                modifier = Modifier.padding(bottom = 32.dp),
                fontWeight = FontWeight.W500
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    BChipButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onCreateTag,
                        text = "+ New Tag",
                        dashedBorderColor = LocalPallet.current
                            .transparent
                            .whiteInvert
                            .tertiary
                            .copy(alpha = 0.1f),
                        painter = null,
                        background = Color.Transparent,
                        contentColor = LocalPallet.current
                            .transparent
                            .whiteInvert
                            .primary
                            .copy(alpha = 0.5f),
                        fontSize = 18.sp
                    )
                }

                items(state.tags) { tag ->
                    val isSelected = state.selectedTag == tag
                    BChipButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onSelect.invoke(tag) },
                        fontSize = 18.sp,
                        text = tag,
                        painter = null,
                        contentColor = when (isSelected) {
                            true -> LocalPallet.current.black.invert

                            false -> LocalPallet.current.white.invert
                        },
                        background = when (isSelected) {
                            true ->
                                LocalPallet.current
                                    .white
                                    .invert

                            false ->
                                LocalPallet.current
                                    .transparent
                                    .whiteInvert
                                    .quaternary
                                    .copy(alpha = 0.05f)
                        },
                    )
                }
                item(span = { GridItemSpan(2) }) {
                    Spacer(Modifier.navigationBarsPadding())
                }
            }
        }
    }
}

// Need to open in interactive mode
@Composable
@Preview
private fun TagsSheetContentPreview() {
    BusyBarThemeInternal {
        Scaffold {
            ModalBottomSheet(
                state = rememberModalBottomSheetState(
                    initialDetent = SheetDetent.FullyExpanded
                ),
                content = {
                    TagsSheetContent(
                        state = TagsViewModel.State(
                            tags = List(4) { "Tag $it" },
                        ),
                        onCreateTag = {},
                        onSelect = {}
                    )
                }
            )
        }
    }
}
