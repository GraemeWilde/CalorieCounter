package com.wilde.caloriecounter2.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.wilde.caloriecounter2.data.food.entities.Product

interface FoodListInterface {
    val foods: LiveData<List<Product>>
}

// Common base class for FoodListViewModel and FoodSearchViewModel
abstract class FoodListViewModelClass: FoodListInterface, ViewModel()