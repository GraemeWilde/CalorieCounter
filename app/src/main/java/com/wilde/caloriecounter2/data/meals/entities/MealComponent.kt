package com.wilde.caloriecounter2.data.meals.entities

import android.os.Parcelable
import androidx.room.*
import com.squareup.moshi.JsonClass
import com.wilde.caloriecounter2.data.other.quantity.Quantity
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass( generateAdapter = true )
@Entity(tableName = "meal_components",
    indices = [
        Index(value = ["meal_id"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = Meal::class,
            parentColumns = ["id"],
            childColumns = ["meal_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
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
    var quantity: Quantity
) : Parcelable