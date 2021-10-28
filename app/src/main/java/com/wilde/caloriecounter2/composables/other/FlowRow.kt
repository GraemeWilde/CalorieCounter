package com.wilde.caloriecounter2.composables.other

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout

@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(content, modifier) { measurables, constraints ->
        val measurableConstraints = constraints.copy(minWidth = 0, minHeight = 0)

        val placeables = measurables.map { measurable ->
            measurable.measure(measurableConstraints)
        }

        var width = 0
        var height = 0

        run {
            var xPosition = 0
            var yPosition = 0
            var lastY = 0

            placeables.forEach { placeable ->
                when {
                    xPosition == 0 -> {
                        xPosition += placeable.width
                        lastY = placeable.height
                    }
                    placeable.width + xPosition < constraints.maxWidth -> {
                        xPosition += placeable.width
                        lastY = placeable.height
                    }
                    else -> {
                        yPosition += lastY
                        xPosition = placeable.width
                        lastY = placeable.height
                    }
                }

                if (xPosition > width) width = xPosition
            }
            height = yPosition + lastY
        }

        width = Integer.min(Integer.max(width, constraints.minWidth), constraints.maxWidth)
        height = Integer.min(Integer.max(height, constraints.minHeight), constraints.maxHeight)

        layout(width, height) {
            var xPosition = 0
            var yPosition = 0
            var lastY = 0

            placeables.forEach { placeable ->
                when {
                    xPosition == 0 -> {
                        placeable.placeRelative(x = 0, y = yPosition)
                        xPosition += placeable.width
                        lastY = placeable.height
                    }
                    placeable.width + xPosition < constraints.maxWidth -> {
                        placeable.placeRelative(xPosition, yPosition)
                        xPosition += placeable.width
                        lastY = placeable.height
                    }
                    else -> {
                        yPosition += lastY
                        placeable.placeRelative(0, yPosition)
                        xPosition = placeable.width
                        lastY = placeable.height
                    }
                }
            }
        }
    }
}