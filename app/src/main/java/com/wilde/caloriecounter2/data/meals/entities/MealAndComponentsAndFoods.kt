package com.wilde.caloriecounter2.data.meals.entities

import androidx.room.Embedded
import androidx.room.Relation
import com.squareup.moshi.JsonClass
import java.io.Serializable

@JsonClass( generateAdapter = true )
data class MealAndComponentsAndFoods(
    @Embedded
    val meal: Meal,

    @Relation(
        entity = MealComponent::class,
        parentColumn = "id",
        entityColumn = "meal_id"
    ) val mealComponentsAndFoods: List<MealComponentAndFood>
)
