package com.wilde.caloriecounter2.composables.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wilde.caloriecounter2.data.meals.entities.MealAndComponentsAndFoods
import com.wilde.caloriecounter2.viewmodels.MealListViewModel

@Composable
fun MealList(mealListViewModel: MealListViewModel = viewModel(), onSelect: ((MealAndComponentsAndFoods) -> Unit)?) {
    val meals = mealListViewModel.mealsList.observeAsState()

    LazyColumn() {
        meals.value?.let {
            items(it) { meal ->
                Text(meal.meal.name, Modifier.clickable {
                    onSelect?.invoke(meal)
                })
            }
        }
    }
}