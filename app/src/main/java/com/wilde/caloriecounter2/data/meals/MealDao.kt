package com.wilde.caloriecounter2.data.meals

import androidx.lifecycle.LiveData
import androidx.room.*
import com.wilde.caloriecounter2.data.meals.entities.*

@Dao
interface MealDao {

    @Query("SELECT * FROM meals")
    fun getMealsNameList(): LiveData<List<Meal>>


    @Transaction
    @Query("SELECT * FROM meals")
    fun getMeals(): LiveData<List<MealAndComponents>>

    /*@Transaction
    @Query("SELECT * FROM meals WHERE id = :id")
    suspend fun getMeal(id: Int): Meal?

    */
    suspend fun insertMeals(vararg mealAndComponents: MealAndComponents): List<Long> {
        return mealAndComponents.map { meal ->
            insertMeal(meal.meal, meal.mealComponents)
        }
    }

    suspend fun updateMeals(vararg mealAndComponents: MealAndComponents) {
        mealAndComponents.forEach { meal ->
            updateMeal(meal.meal, meal.mealComponents)
        }
    }


    @Transaction
    suspend fun insertMeal(meal: Meal, mealComponents: List<MealComponent>): Long {
        val id = insertMealParents(meal)

        val comps = mealComponents.map { component ->
            component.copy(mealId = id[0].toInt())
        }

        insertMealComponentsRefs(comps)

        return id[0]
    }

    @Transaction
    suspend fun updateMeal(meal: Meal, mealComponents: List<MealComponent>) {
        updateMealParents(meal)

        mealComponents.forEach {
            if (it.id != 0) {
                updateMealComponentsRefs(it)
            } else {
                insertMealComponentsRefs(it)
            }
        }
        updateMealComponentsRefs(mealComponents)
    }


    @Insert
    suspend fun insertMealParents(vararg meals: Meal): List<Long>

    @Update
    suspend fun updateMealParents(vararg meals: Meal)
    /*
    @Insert
    suspend fun insertMealParents(mealParents: List<mealParent>): List<Long>

    @Insert
    suspend fun insertMealComponentsRefs(vararg mealComponentRefs: MealComponentRef)
    */
    @Insert
    suspend fun insertMealComponentsRefs(mealComponentRefs: List<MealComponent>)

    @Insert
    suspend fun insertMealComponentsRefs(vararg mealComponentRefs: MealComponent)

    @Update
    suspend fun updateMealComponentsRefs(mealComponentRefs: List<MealComponent>)

    @Update
    suspend fun updateMealComponentsRefs(vararg mealComponentRefs: MealComponent)
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

    @Delete
    suspend fun deleteMeals(vararg meals: Meal)
}