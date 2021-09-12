package com.wilde.caloriecounter2.data.meals

import androidx.lifecycle.LiveData
import com.wilde.caloriecounter2.data.meals.entities.MealAndComponent
import com.wilde.caloriecounter2.data.meals.entities.Meal
import com.wilde.caloriecounter2.data.meals.entities.MealAndComponentsAndFoods
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MealRepository @Inject constructor(
    private val mealDao: MealDao
) {
    fun getMeals(): LiveData<List<MealAndComponent>> {
        return mealDao.getMeals()
    }

    fun getMealsNamesList(): LiveData<List<Meal>> {
        return mealDao.getMealsNameList()
    }

    fun getMealsAndComponentsAndFoods(): LiveData<List<MealAndComponentsAndFoods>> {
        return mealDao.getMealsAndComponentsAndFoods()
    }
}