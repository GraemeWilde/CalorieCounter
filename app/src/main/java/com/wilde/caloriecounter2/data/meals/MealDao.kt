package com.wilde.caloriecounter2.data.meals

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.*
import com.wilde.caloriecounter2.data.meals.entities.*

@Dao
interface MealDao {

    @Query("SELECT * FROM meals")
    fun getMealsNameList(): LiveData<List<Meal>>


    @Transaction
    @Query("SELECT * FROM meals")
    fun getMeals(): LiveData<List<MealAndComponent>>

    /*@Transaction
    @Query("SELECT * FROM meals WHERE id = :id")
    suspend fun getMeal(id: Int): Meal?

    */
    suspend fun insertMeals(vararg mealAndComponents: MealAndComponent) {
        mealAndComponents.forEach { meal ->
            insertMeal(meal.meal, meal.mealComponents)
        }
    }


    @Transaction
    suspend fun insertMeal(meal: Meal, mealComponent: List<MealComponent>) {
        val id = insertMealParents(meal)
        Log.d("MealDAO", id[0].toInt().toString())

        val comps = mealComponent.map { component ->
            component.copy(mealId = id[0].toInt())
        }

        Log.d("MealDAO", comps.toString())

        insertMealComponentsRefs(comps)
    }


    @Insert
    suspend fun insertMealParents(vararg meals: Meal): List<Long>
    /*
    @Insert
    suspend fun insertMealParents(mealParents: List<mealParent>): List<Long>

    @Insert
    suspend fun insertMealComponentsRefs(vararg mealComponentRefs: MealComponentRef)
    */
    @Insert
    suspend fun insertMealComponentsRefs(mealComponentRefs: List<MealComponent>)
    /*

    @Update
    suspend fun updateMealParent(mealParent: mealParent): Int

    @Update
    suspend fun updateMealComponents(vararg mealComponentRefs: MealComponentRef)


    @Delete
    suspend fun deleteMealParent(mealParent: mealParent)

    @Delete
    suspend fun deleteMealComponents(vararg mealComponentRefs: MealComponentRef)*/

    @Transaction
    @Query("SELECT * FROM meal_components WHERE id = :id")
    fun getMealComponentAndFood(id: Int): LiveData<MealComponentAndFood>

    @Transaction
    @Query("SELECT * FROM meals WHERE id = :id")
    fun getMealAndComponentsAndFoods(id: Int): LiveData<MealAndComponentsAndFoods>

    @Transaction
    @Query("SELECT * FROM meals")
    fun getMealsAndComponentsAndFoods(): LiveData<List<MealAndComponentsAndFoods>>
}