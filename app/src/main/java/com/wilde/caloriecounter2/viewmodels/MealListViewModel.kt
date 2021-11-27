package com.wilde.caloriecounter2.viewmodels

import androidx.lifecycle.ViewModel
import com.wilde.caloriecounter2.data.meals.MealRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MealListViewModel @Inject internal constructor(
    private val mealRepository: MealRepository
): ViewModel() {
    //private var meals: LiveData<List<Product>>

    //val mealsList = mealRepository.getMeals()
    val mealsList = mealRepository.getMealsAndComponentsAndFoods()

}