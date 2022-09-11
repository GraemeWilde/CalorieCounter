package com.wilde.caloriecounter2.composables.other

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.*
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    paddingBetweenHorizontal: Dp = 0.dp,
    paddingBetweenVertical: Dp = 0.dp,
    content: @Composable () -> Unit
) {

    val measurePolicy = object : MeasurePolicy {
        override fun MeasureScope.measure(
            measurables: List<Measurable>,
            constraints: Constraints
        ): MeasureResult {
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
                    //Log.d("FlowRow", "Placable Width: ${placeable.width}")
                    when {
                        xPosition == 0 -> {
                            xPosition += placeable.width
                            lastY = placeable.height
                        }
                        placeable.width + xPosition < constraints.maxWidth -> {
                            xPosition += placeable.width
                            if (placeable.height > lastY) {
                                lastY = placeable.height
                            }
                        }
                        else -> {
                            yPosition += lastY + paddingBetweenVertical.roundToPx()
                            xPosition = placeable.width
                            lastY = placeable.height
                        }
                    }

                    if (xPosition > width) width = xPosition
                    if (xPosition > 0) xPosition += paddingBetweenHorizontal.roundToPx()
                }
                height = yPosition + lastY
            }

            width = Integer.min(Integer.max(width, constraints.minWidth), constraints.maxWidth)
            height = Integer.min(Integer.max(height, constraints.minHeight), constraints.maxHeight)

            return layout(width, height) {
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
                            if (placeable.height > lastY) {
                                lastY = placeable.height
                            }
                        }
                        else -> {
                            yPosition += lastY + paddingBetweenVertical.roundToPx()
                            placeable.placeRelative(0, yPosition)
                            xPosition = placeable.width
                            lastY = placeable.height
                        }
                    }
                    if (xPosition > 0) xPosition += paddingBetweenHorizontal.roundToPx()
                }
            }
        }

        override fun IntrinsicMeasureScope.minIntrinsicHeight(
            measurables: List<IntrinsicMeasurable>,
            width: Int
        ): Int {
            data class Dimension(val width: Int, val height: Int)

            val placables = measurables.map {
                val calcHeight = it.minIntrinsicHeight(width)
                val calcWidth = it.maxIntrinsicWidth(calcHeight)

                Dimension(calcWidth, calcHeight)
            }

            var xPosition = 0
            var yPosition = 0
            var lastY = 0

            var containerWidth = 0

            placables.forEach { placeable ->
                when {
                    xPosition == 0 -> {
                        xPosition += placeable.width
                        lastY = placeable.height
                    }
                    placeable.width + xPosition < width -> {
                        xPosition += placeable.width
                        if (placeable.height > lastY) {
                            lastY = placeable.height
                        }
                    }
                    else -> {
                        yPosition += lastY + paddingBetweenVertical.roundToPx()
                        xPosition = placeable.width
                        lastY = placeable.height
                    }
                }

                if (xPosition > containerWidth) containerWidth = xPosition
                if (xPosition > 0) xPosition += paddingBetweenHorizontal.roundToPx()
            }

            return yPosition + lastY
        }
    }

    Layout(content, modifier, measurePolicy)
}