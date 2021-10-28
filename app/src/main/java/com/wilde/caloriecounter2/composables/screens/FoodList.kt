package com.wilde.caloriecounter2.composables.screens

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
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.wilde.caloriecounter2.composables.other.FlowRow
import com.wilde.caloriecounter2.data.food.entities.Product
import com.wilde.caloriecounter2.viewmodels.FoodListInterface
import com.wilde.caloriecounter2.viewmodels.FoodListViewModel
import com.wilde.caloriecounter2.viewmodels.FoodListViewModelClass


data class FoodListColors(
    val foodBorderColor: Color = Color(0xFFCCCCCC),
    val textColor: Color = Color(0xFFEEEEEE),
    val productNameColor: Color = Color(0xFF333333),
    val productBrandColor: Color = Color(0xFF663333),
    val productQuantityColor: Color = Color(0xFF333366)
)


@Composable
fun FoodList(
    viewModel: FoodListViewModelClass = hiltViewModel<FoodListViewModel>(),
    foodListColors: FoodListColors = FoodListColors(),
    onSelect: ((selectedFood: Product) -> Unit)? = null
) {
    //val viewModel = viewModel(FoodListViewModel::class.java)

    val foods = viewModel.foods.observeAsState()

    FoodListContent(foods.value, foodListColors, onSelect)
}

@Composable
fun FoodListContent(
    foods: List<Product>?,
    foodListColors: FoodListColors = FoodListColors(),
    onSelect: ((selectedFood: Product) -> Unit)? = null
) {
    LazyColumn(
        Modifier.fillMaxWidth()
    ) {
        if (foods != null) items(foods) { food ->
            Surface(
                Modifier
                    .padding(8.dp, 2.dp)
                    .then(if (onSelect != null) Modifier.clickable {
                        onSelect(food)
                    } else
                        Modifier),
                shape = RoundedCornerShape(15.dp),
                border = BorderStroke(2.dp, foodListColors.foodBorderColor)
            ) {
                Row(Modifier.padding(end = 3.dp, top = 4.dp, bottom = 4.dp)) {
                    Box(
                        Modifier
                            .padding(start = 5.dp)
                            .size(12.dp, 20.dp)
                            .align(Alignment.CenterVertically)
                    ) {
                        Icon(
                            Icons.Filled.ChevronRight, null,
                            Modifier.requiredSize(24.dp)
                        )
                    }
                    FlowRow(
                        Modifier
                            .fillMaxWidth()
                        /*.then(
                            if (onSelect != null) Modifier.clickable {
                                onSelect(food)
                            } else
                                Modifier
                        )*/
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
                        food.packageSize?.let {
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
        shape = RoundedCornerShape(21.dp / 2),
    ) {
        Text(text, Modifier.padding(7.dp, 0.dp))
    }
}