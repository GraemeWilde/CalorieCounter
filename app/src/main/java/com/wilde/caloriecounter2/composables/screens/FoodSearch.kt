package com.wilde.caloriecounter2.composables.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import com.wilde.caloriecounter2.data.food.entities.Product
import com.wilde.caloriecounter2.viewmodels.FoodSearchViewModel

@Composable
fun SearchList(
    viewModel: FoodSearchViewModel,
    foodListColors: FoodListColors = FoodListColors(),
    onSelect: ((selectedFood: Product) -> Unit)? = null
) {
    val foods = viewModel.foods.observeAsState()

    FoodListContent(foods.value, foodListColors, onSelect)
}