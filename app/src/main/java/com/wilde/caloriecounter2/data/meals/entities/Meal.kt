package com.wilde.caloriecounter2.data.meals.entities

import androidx.room.Embedded
import androidx.room.Relation

data class Meal(
    @Embedded val mealParent: MealParent,
    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    )
    val mealComponents: List<MealComponent>
)