package com.flipperdevices.bsb.timer.cards.model

import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize

internal data class LayoutData(
    val size: DpSize,
    val offset: DpOffset
) {
    companion object {
        val Zero: LayoutData
            get() = LayoutData(
                size = DpSize.Zero,
                offset = DpOffset.Zero
            )
    }
}

internal fun LayoutCoordinates.asLayoutData(localDensity: Density): LayoutData {
    return with(localDensity) {
        LayoutData(
            size = DpSize(
                width = this@asLayoutData.size.width.toDp(),
                height = this@asLayoutData.size.height.toDp()
            ),
            offset = DpOffset(
                x = this@asLayoutData.positionInParent().x.toDp(),
                y = this@asLayoutData.positionInParent().x.toDp()
            )
        )
    }
}