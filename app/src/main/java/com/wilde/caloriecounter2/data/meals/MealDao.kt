package com.wilde.caloriecounter2.data.meals

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.*
import com.wilde.caloriecounter2.data.meals.entities.Meal
import com.wilde.caloriecounter2.data.meals.entities.MealComponent
import com.wilde.caloriecounter2.data.meals.entities.MealParent

@Dao
interface MealDao {

    @Query("SELECT * FROM meals")
    fun getMealsNameList(): LiveData<List<MealParent>>

    @Transaction
    @Query("SELECT * FROM meals")
    fun getMeals(): LiveData<List<Meal>>

    @Transaction
    @Query("SELECT * FROM meals WHERE id = :id")
    suspend fun getMeal(id: Int): Meal?


    suspend fun insertMeals(vararg meals: Meal) {
        meals.forEach { meal ->
            insertMeal(meal.mealParent, meal.mealComponents)
        }
    }

    /*@Transaction
    suspend fun insertMeal(meal: Meal) {

    }*/

    @Transaction
    suspend fun insertMeal(mealParent: MealParent, mealComponents: List<MealComponent>) {
        val id = insertMealParents(mealParent)
        Log.d("MealDAO", id[0].toInt().toString())

        val comps = mealComponents.map { component ->
            component.copy(mealId = id[0].toInt())
        }

        Log.d("MealDAO", comps.toString())

        insertMealComponents(comps)
    }

    @Insert
    suspend fun insertMealParents(vararg mealParents: MealParent): List<Long>

    @Insert
    suspend fun insertMealParents(mealParents: List<MealParent>): List<Long>

    @Insert
    suspend fun insertMealComponents(vararg mealComponents: MealComponent)

    @Insert
    suspend fun insertMealComponents(mealComponents: List<MealComponent>)


    @Update
    suspend fun updateMealParent(mealParent: MealParent): Int

    @Update
    suspend fun updateMealComponents(vararg mealComponents: MealComponent)


    @Delete
    suspend fun deleteMealParent(mealParent: MealParent)

    @Delete
    suspend fun deleteMealComponents(vararg mealComponents: MealComponent)
}