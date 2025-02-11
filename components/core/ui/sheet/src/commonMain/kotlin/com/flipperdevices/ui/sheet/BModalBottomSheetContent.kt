package com.flipperdevices.ui.sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.composables.core.BottomSheetScope
import com.composables.core.DragIndication
import com.composables.core.ModalBottomSheetScope
import com.composables.core.Scrim
import com.composables.core.Sheet

@Composable
// Here modifier is missing is intentional. As it represents itself basic design of BottomSheet
@Suppress("ModifierMissing")
fun ModalBottomSheetScope.BModalBottomSheetContent(
    background: Color = Color(color = 0xFF1E1E1E), // todo
    content: @Composable BottomSheetScope.() -> Unit
) {
    Scrim()
    Sheet(
        modifier = Modifier
            .padding(top = 12.dp)
            .statusBarsPadding()
            .padding(
                WindowInsets.navigationBars.only(WindowInsetsSides.Horizontal).asPaddingValues()
            )
            .shadow(4.dp, RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
            .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
            .background(background) // todo
            .padding(horizontal = 24.dp)
            .fillMaxWidth()
            .imePadding(),
    ) {
        Column {
            DragIndication()

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                contentAlignment = Alignment.Center,
                content = { BSheetDragIndicator() }
            )
            content.invoke(this@Sheet)
        }
    }
}
