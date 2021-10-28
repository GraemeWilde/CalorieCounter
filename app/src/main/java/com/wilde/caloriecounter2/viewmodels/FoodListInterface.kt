package com.wilde.caloriecounter2.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.wilde.caloriecounter2.data.food.entities.Product

interface FoodListInterface {
    val foods: LiveData<List<Product>>
}

abstract class FoodListViewModelClass: FoodListInterface, ViewModel()