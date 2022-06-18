package com.wilde.caloriecounter2.data.meals.entities

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import com.squareup.moshi.JsonClass
import com.wilde.caloriecounter2.data.food.entities.Product
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass( generateAdapter = true )
data class MealComponentAndFood(
    @Embedded
    val mealComponent: MealComponent,

    @Relation(
        parentColumn = "food_id",
        entityColumn = "id"
    ) val food: Product
) : Parcelable
