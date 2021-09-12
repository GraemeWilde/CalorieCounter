package com.wilde.caloriecounter2.data.meals.entities

import androidx.room.Embedded
import androidx.room.Relation
import com.wilde.caloriecounter2.data.food.entities.Product

data class MealComponentAndFood(
    @Embedded
    val mealComponent: MealComponent,

    @Relation(
        parentColumn = "food_id",
        entityColumn = "id"
    ) val food: Product
)
