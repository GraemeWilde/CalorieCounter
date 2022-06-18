package com.wilde.caloriecounter2.data.meals

import android.util.Log
import androidx.lifecycle.LiveData
import com.wilde.caloriecounter2.data.meals.entities.Meal
import com.wilde.caloriecounter2.data.meals.entities.MealAndComponents
import com.wilde.caloriecounter2.data.meals.entities.MealAndComponentsAndFoods
import com.wilde.caloriecounter2.data.meals.entities.MealComponent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MealRepository @Inject constructor(
    private val mealDao: MealDao
) {
    fun getMeals(): LiveData<List<MealAndComponents>> {
        return mealDao.getMeals()
    }

    fun getMealsNamesList(): LiveData<List<Meal>> {
        return mealDao.getMealsNameList()
    }

    fun getMealsAndComponentsAndFoods(): LiveData<List<MealAndComponentsAndFoods>> {
        return mealDao.getMealsAndComponentsAndFoods()
    }

    suspend fun insertMealAndComponents(mealAndComponents: MealAndComponents) {
        mealDao.insertMeals(mealAndComponents)
    }

    suspend fun updateMealAndComponents(mealAndComponents: MealAndComponents) {
        mealDao.updateMeals(mealAndComponents)
    }

    suspend fun removeMeals(vararg meals: Meal) {
        mealDao.deleteMeals(*meals)
    }

    suspend fun removeMeal(meal: Meal) {
        mealDao.deleteMeal(meal)
    }

    suspend fun deleteMealComponentsByIds(vararg mealComponents: Int): Int {
        return mealDao.deleteMealComponentsByIds(*mealComponents)
    }
}