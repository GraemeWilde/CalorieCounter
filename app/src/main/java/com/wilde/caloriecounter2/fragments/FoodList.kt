package com.wilde.caloriecounter2.fragments

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wilde.caloriecounter2.data.food.entities.Product
import com.wilde.caloriecounter2.viewmodels.FoodListViewModel
import java.lang.Integer.max
import java.lang.Integer.min


data class FoodListColors(
    val foodBorderColor: Color = Color(0xFFCCCCCC),
    val textColor: Color = Color(0xFFEEEEEE),
    val productNameColor: Color = Color(0xFF333333),
    val productBrandColor: Color = Color(0xFF663333),
    val productQuantityColor: Color = Color(0xFF333366)
)


@Composable
fun FoodList(/*viewModel: FoodListViewModel,*/ foodListColors: FoodListColors = FoodListColors(), onSelect: ((selectedFood: Product) -> Unit)? = null) {
    val viewModel = viewModel(FoodListViewModel::class.java)

    val foods = viewModel.foods.observeAsState()

    FoodListContent(foods.value, foodListColors, onSelect)
}

@Composable
fun FoodListContent(foods: List<Product>?, foodListColors: FoodListColors = FoodListColors(), onSelect: ((selectedFood: Product) -> Unit)? = null) {
    LazyColumn(
        Modifier.fillMaxWidth()
    ) {
        if (foods != null) items(foods) { food ->
            Surface(
                Modifier.padding(8.dp, 2.dp),
                shape = RoundedCornerShape(15.dp),
                border = BorderStroke(2.dp, foodListColors.foodBorderColor)
            ) {
                Row(Modifier.padding(end = 3.dp, top = 4.dp, bottom = 4.dp)) {
                    Icon(Icons.Filled.ChevronRight, null,
                        Modifier

                            //.padding(0.dp, 2.dp)
                            //.size(20.dp)
                            .align(Alignment.CenterVertically)
                    )
                    FlowRow(
                        Modifier
                            .fillMaxWidth()
                            .then(
                                if (onSelect != null) Modifier.clickable {
                                    onSelect(food)
                                } else
                                    Modifier
                            )
                            //.padding(0.dp, 1.dp)
                    ) {
                        TextBubble(
                            food.productName,
                            foodListColors.productNameColor,
                            foodListColors.textColor
                        )
                        if (food.brands.isNotBlank()) {
                            TextBubble(
                                food.brands,
                                foodListColors.productBrandColor,
                                foodListColors.textColor
                            )
                        }
                        food.quantity?.let {
                            TextBubble(
                                it,
                                foodListColors.productQuantityColor,
                                foodListColors.textColor
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TextBubble(text: String, color: Color, contentColor: Color) {
    Surface(
        Modifier.padding(2.dp, 1.dp),
        color = color,
        contentColor = contentColor,
        shape = RoundedCornerShape(21.dp/2),
    ) {
        Text(text, Modifier.padding(7.dp, 0.dp))
    }
}

@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(content, modifier) { measurables, constraints ->
        Log.d("Placeable", "FlowRow ${measurables.size}")

        val measurableConstraints = constraints.copy(minWidth = 0, minHeight = 0)

        val placeables = measurables.map { measurable ->
            measurable.measure(measurableConstraints)
        }
        Log.d("Placeable", "Before layout")

        var width = 0
        var height = 0

        run {
            var xPosition = 0
            var yPosition = 0
            var lastY = 0

            placeables.forEach { placeable ->
                if (xPosition == 0) {
                    //placeable.placeRelative(x = 0, y = yPosition)
                    xPosition += placeable.width
                    lastY = placeable.height
                } else if (placeable.width + xPosition < constraints.maxWidth) {
                    //placeable.placeRelative(xPosition, yPosition)
                    xPosition += placeable.width
                    lastY = placeable.height
                } else {
                    yPosition += lastY
                    //placeable.placeRelative(0, yPosition)
                    xPosition = placeable.width
                    lastY = placeable.height
                }

                if (xPosition > width) width = xPosition
            }
            height = yPosition + lastY
        }

        width = min(max(width, constraints.minWidth), constraints.maxWidth)
        height = min(max(height, constraints.minHeight), constraints.maxHeight)

        layout(width, height) {
            var xPosition = 0
            var yPosition = 0
            var lastY = 0

            Log.d("Placeable", "layout")

            placeables.forEach { placeable ->
                if (xPosition == 0) {
                    placeable.placeRelative(x = 0, y = yPosition)
                    xPosition += placeable.width
                    lastY = placeable.height
                } else if (placeable.width + xPosition < constraints.maxWidth) {
                    placeable.placeRelative(xPosition, yPosition)
                    xPosition += placeable.width
                    lastY = placeable.height
                } else {
                    yPosition += lastY
                    placeable.placeRelative(0, yPosition)
                    xPosition = placeable.width
                    lastY = placeable.height
                }
            }


        }
    }
}