package com.wilde.caloriecounter2.data.journal.entities

import androidx.room.Embedded
import androidx.room.Relation
import com.wilde.caloriecounter2.data.food.entities.Product
import com.wilde.caloriecounter2.data.meals.entities.Meal
import com.wilde.caloriecounter2.data.meals.entities.MealAndComponentsAndFoods
import com.wilde.caloriecounter2.data.meals.entities.MealComponent

data class FullJournalEntry(
    @Embedded
    val journalEntry: JournalEntry,

    @Relation(
        entity = Meal::class,
        parentColumn = "meal_id",
        entityColumn = "id"
    )
    val mealAndComponentsAndFoods: MealAndComponentsAndFoods?,

    @Relation(
        parentColumn = "food_id",
        entityColumn = "id"
    )
    val food: Product?
)