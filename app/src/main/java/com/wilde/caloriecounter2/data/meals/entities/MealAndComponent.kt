package com.wilde.caloriecounter2.data.meals.entities

import androidx.room.Embedded
import androidx.room.Relation
import java.io.Serializable

data class MealAndComponent(
    @Embedded val meal: Meal,
    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    )
    val mealComponents: List<MealComponent>
) : Serializable