package com.wilde.caloriecounter2.data.journal

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import com.wilde.caloriecounter2.data.other.quantity.Quantity
import java.time.LocalDateTime

@JsonClass( generateAdapter = true)
@Entity(tableName = "journal_entries")
data class entry(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "date")
    val date: LocalDateTime,

    @ColumnInfo(name = "food_id")
    val foodId: Int,

    @ColumnInfo(name = "meal_id")
    val mealId: Int,

    @Embedded
    val quantity: Quantity
)
