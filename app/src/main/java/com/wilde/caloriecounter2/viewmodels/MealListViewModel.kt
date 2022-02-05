package com.wilde.caloriecounter2.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wilde.caloriecounter2.data.meals.MealRepository
import com.wilde.caloriecounter2.data.meals.entities.Meal
import com.wilde.caloriecounter2.data.meals.entities.MealAndComponentsAndFoods
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealListViewModel @Inject internal constructor(
    private val mealRepository: MealRepository
): ViewModel() {
    //private var meals: LiveData<List<Product>>

    //val mealsList = mealRepository.getMeals()
    val mealsList = mealRepository.getMealsAndComponentsAndFoods()

    fun removeMeals(vararg meals: Meal) {
        viewModelScope.launch {
            mealRepository.removeMeals(*meals)
        }
    }
}