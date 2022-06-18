package com.wilde.caloriecounter2.data.journal.entities

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import com.squareup.moshi.JsonClass
import com.wilde.caloriecounter2.data.food.entities.Product
import com.wilde.caloriecounter2.data.meals.entities.Meal
import com.wilde.caloriecounter2.data.meals.entities.MealAndComponentsAndFoods
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
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
) : Parcelable