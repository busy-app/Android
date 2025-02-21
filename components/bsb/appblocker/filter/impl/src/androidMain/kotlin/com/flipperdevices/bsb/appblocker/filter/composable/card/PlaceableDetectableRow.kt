package com.flipperdevices.bsb.appblocker.filter.composable.card

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable

private data class Item(val placeable: Placeable, val xPosition: Int)

@Composable
fun PlaceableDetectableRow(
    onPlacementComplete: (Int) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        val items = mutableListOf<Item>()
        var xPosition = 0
        for (measurable in measurables) {
            val placeable = measurable.measure(constraints)
            if (xPosition + placeable.width > constraints.maxWidth) break
            items.add(Item(placeable, xPosition))
            xPosition += placeable.width
        }

        layout(
            width = items.last().let { it.xPosition + it.placeable.width },
            height = items.maxOf { it.placeable.height }
        ) {
            items.forEach {
                it.placeable.place(it.xPosition, 0)
            }
            onPlacementComplete(items.count())
        }
    }
}
