package com.wilde.caloriecounter2.data.meals.entities

import androidx.room.*

@Entity(tableName = "mealComponents",
    indices = [
        Index(value = ["meal_id"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = MealParent::class,
            parentColumns = ["id"],
            childColumns = ["meal_id"],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class MealComponent(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "meal_id")
    val mealId: Int = 0,

    @ColumnInfo(name = "food_id")
    val foodId: Int = 0,

    @Embedded
    val quantity: Quantity
)