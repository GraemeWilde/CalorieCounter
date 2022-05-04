package com.wilde.caloriecounter2.composables.other

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.*
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class VerticalAlignment {
    Start,
    End,
    Center
}

@Composable
fun PairsGrid(
    modifier: Modifier = Modifier,
    paddingBetween: ((column: Int, columnCount: Int) -> Int)? = null,
    verticalAlignment: ((column: Int, columnCount: Int, row: Int, rowCount: Int) -> VerticalAlignment)? = null,
    content: @Composable () -> Unit
) {
    data class Dimension(val width: Int, val height: Int)

    val measurePolicy = object : MeasurePolicy {

        fun measurements(measures: List<Dimension>, maxWidth: Int, paddingBetween: ((column: Int, columnCount: Int) -> Int)? = null) = object {
            val width: Int
            val height: Int
            val rowHeights: List<Int>
            val colWidths: List<Int>
            val columnCount: Int

            init {
                val measuresByWidth = (measures.indices).sortedByDescending {
                    Log.d("PairsGrid", "Width[$it]: ${measures[it].width} Height[$it]: ${measures[it].height}")
                    measures[it].width
                }

                Log.d("PairsGrid", "mbw: ${measuresByWidth.toString()}")

                var good = true
                var colsStep = 2
                var lastColWidths: List<Int> = listOf(measures[measuresByWidth[0]].width)
                var lastWidth = measures[measuresByWidth[0]].width

                // Figure out how many pairs of columns will fit
                while (good) {
                    val findCols: MutableList<Int> = (0 until colsStep).toMutableList()
                    val colWidths: Array<Int> = Array(colsStep) { -1 }
                    var fullWidth = 0

                    var i = 0
                    while (findCols.isNotEmpty() && i < measuresByWidth.size) {
                        val index = measuresByWidth[i]
                        val col = index % colsStep
                        if (findCols.contains(col)) {
                            //Log.d("PairsGrid", "Contains $i in $col")
                            colWidths[col] = measures[index].width
                            //fullWidth += colWidths[col] + (paddingBetween.takeUnless { col == colsStep - 1 } ?: 0)
                            fullWidth += colWidths[col] + if (col == colsStep - 1 || paddingBetween == null) 0 else paddingBetween(col, colsStep)
                            Log.d("PairsGrid", "Contains $i in $col - cw: ${colWidths[col]} - pd: ${if (col == colsStep - 1 || paddingBetween == null) 0 else paddingBetween(col, colsStep)}")
                            findCols.remove(col)
                        }
                        i++
                    }

                    if (findCols.isNotEmpty() || fullWidth > maxWidth) {
                        if (colsStep == 2)
                            colsStep--
                        else
                            colsStep -= 2
                        good = false
                    } else {
                        lastColWidths = colWidths.toList()
                        lastWidth = fullWidth
                        colsStep += 2
                    }
                    Log.d("PairsGrid", "lcw: ${lastColWidths.toString()}")
                }

                var height = 0
                val rowHeights: MutableList<Int> =
                    MutableList((measures.size - 1) / colsStep + 1) { 0 }
                run {
                    var thisLineHeight = 0
                    var row = 0

                    Log.d("PairsGrid", "colsStep: $colsStep")

                    measures.forEachIndexed { index, measure ->
                        Log.d(
                            "PairsGrid",
                            "MeasureHeight: ${measure.height} - LineHeight: $thisLineHeight"
                        )
                        if (measure.height > thisLineHeight) thisLineHeight = measure.height

                        if ((index + 1) % colsStep == 0 || index == measures.size - 1) {
                            Log.d("PairsGrid", "Row #: $row - Index: $index")
                            height += thisLineHeight
                            rowHeights[row] = thisLineHeight
                            thisLineHeight = 0
                            Log.d("PairsGrid", "Row: $thisLineHeight")
                            row++
                        }
                    }
                }

                Log.d("PairsGrid", "Meas Height: $height")

                this.width = lastWidth
                this.height = height
                this.colWidths = lastColWidths
                this.rowHeights = rowHeights
                this.columnCount = colsStep
            }
        }

        override fun MeasureScope.measure(
            measurables: List<Measurable>,
            constraints: Constraints
        ): MeasureResult {
            val measurableConstraints = constraints.copy(minWidth = 0, minHeight = 0)

            val placeables = measurables.map { measurable ->
                measurable.measure(measurableConstraints)
            }

            val measures = placeables.map { placeable ->
                Dimension(placeable.width, placeable.height)
            }
            Log.d("PairsGrid", "Constraints H: ${constraints.maxHeight}")

//            val measuresByWidth = (measures.indices).sortedByDescending {
//                measures[it].width
//            }
//
//            var good = true
//            var colsStep = 2
//            var lastColWidths: List<Int> = listOf(measures[measuresByWidth[0]].width)
//            var lastWidth = measures[measuresByWidth[0]].width
//
//            // Figure out how many pairs of columns will fit
//            while (good) {
//                val findCols: MutableList<Int> = (0 until colsStep).toMutableList()
//                val colWidths: Array<Int> = Array(colsStep) { -1 }
//                var maxWidth = 0
//
//                var i = 0
//                while (findCols.isNotEmpty() && i < measuresByWidth.size) {
//                    val col = measuresByWidth[i] % colsStep
//                    if (findCols.contains(col)) {
//                        colWidths[col] = measures[col].width + (paddingBetween.roundToPx().takeUnless { col == colsStep - 1 } ?: 0)
//                        maxWidth += colWidths[col]
//                        findCols.remove(col)
//                    }
//                    i++
//                }
//
//                if (findCols.isNotEmpty() || maxWidth > constraints.maxWidth) {
//                    if (colsStep == 2)
//                        colsStep--
//                    else
//                        colsStep -= 2
//                    good = false
//                } else {
//                    lastColWidths = colWidths.toList()
//                    lastWidth = maxWidth
//                    colsStep += 2
//                }
//            }
//
//            var height = 0
//            val rowHeights: MutableList<Int> = MutableList((measures.size - 1) / colsStep + 1) { 0 }
//            run {
//                var thisLineHeight = 0
//                var row = 0
//
//                Log.d("PairsGrid", "colsStep: $colsStep")
//
//                measures.forEachIndexed { index, measure ->
//                    Log.d("PairsGrid", "MeasureHeight: ${measure.height} - LineHeight: $thisLineHeight")
//                    if (measure.height > thisLineHeight) thisLineHeight = measure.height
//
//                    if ((index + 1) % colsStep == 0 || index == measures.size - 1) {
//                        Log.d("PairsGrid", "Row #: $row - Index: $index")
//                        height += thisLineHeight
//                        rowHeights[row] = thisLineHeight
//                        thisLineHeight = 0
//                        Log.d("PairsGrid", "Row: $thisLineHeight")
//                        row++
//                    }
//                }
//            }


            val meas = measurements(measures, constraints.maxWidth, paddingBetween)

            val placeOffsets: List<Int> = run {
                var cumulativeOffset = 0
                meas.colWidths.mapIndexed { index, it ->
                    if (index == 0) {
                        Log.d("PairsGrid", "placeOffset[$index]: 0")
                        0
                    } else {
                        Log.d("PairsGrid", "placeOffset[$index]: ${meas.colWidths[index - 1]}")
                        cumulativeOffset += meas.colWidths[index - 1] + if (paddingBetween == null) 0 else paddingBetween(index - 1, meas.columnCount) /*.takeUnless { index == meas.columnCount - 1 } ?: 0)*/
                        cumulativeOffset
                    }
                }
            }

            val width = Integer.min(Integer.max(meas.width, constraints.minWidth), constraints.maxWidth)
            val height = Integer.min(Integer.max(meas.height, constraints.minHeight), constraints.maxHeight)

            Log.d("PairsGrid", "height: $height")

            return layout(width, height) {
                run {
                    var row = 0
                    var cumulativeHeight = 0

                    placeables.forEachIndexed { index, placeable ->
                        val col = index % meas.columnCount
//                        if (col == 0) {
//                            row++
//                        }

                        Log.d("PairsGrid", "RowHeights[$row]: ${meas.rowHeights[row]} - ${placeable.height} - ${(meas.rowHeights[row] - placeable.height) / 2}")

                        val vAlign = if (verticalAlignment != null) {
                            verticalAlignment(col, meas.columnCount, row, meas.rowHeights.size)
                        } else {
                            VerticalAlignment.Start
                        }

                        val x = when(vAlign) {
                            VerticalAlignment.Start -> placeOffsets[col]
                            VerticalAlignment.Center -> (meas.colWidths[col] - placeable.width) / 2 + placeOffsets[col]
                            VerticalAlignment.End -> meas.colWidths[col] - placeable.width + placeOffsets[col]
                        }

                        placeable.placeRelative(x, cumulativeHeight + (meas.rowHeights[row] - placeable.height) / 2)

                        if (col + 1 == meas.columnCount) {
                            cumulativeHeight += meas.rowHeights[row]
                            row++
                        }
                    }
                }
            }
        }

        override fun IntrinsicMeasureScope.minIntrinsicHeight(
            measurables: List<IntrinsicMeasurable>,
            width: Int
        ): Int {
            //Log.d("PairsGridM", "RPX: ${48.dp.roundToPx()}")
            val measures = measurables.map {
                val calcHeight = it.minIntrinsicHeight(width)
                val calcWidth = it.maxIntrinsicWidth(calcHeight)
                //Log.d("PairsGridM", "${it.minIntrinsicHeight(width)} - ${it.maxIntrinsicHeight(width)} - ${it.minIntrinsicWidth(calcHeight)} - ${it.maxIntrinsicWidth(calcHeight)}")

                Dimension(calcWidth, calcHeight)
            }

            val h = measurements(measures, width, paddingBetween).height

            //Log.d("PairsGrid", "Meas H: $h")

            return h
        }
    }

    Layout(content, modifier, measurePolicy)
}