package com.wilde.caloriecounter2.data.journal.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import com.wilde.caloriecounter2.data.food.entities.Product
import com.wilde.caloriecounter2.data.meals.entities.Meal
import com.wilde.caloriecounter2.data.other.quantity.Quantity
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
@JsonClass(generateAdapter = true)
@Entity(tableName = "journal_entries")
data class JournalEntry constructor(
    @ColumnInfo(name = "date")
    val date: LocalDateTime,

    @Embedded
    val quantity: Quantity,

    @ColumnInfo(name = "food_id")
    val foodId: Int?,

    @ColumnInfo(name = "meal_id")
    val mealId: Int?,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
): Parcelable {
    init {
        assert((foodId != null) != (mealId != null))
    }

    constructor(
        date: LocalDateTime,
        quantity: Quantity,
        product: Product,
        id: Int = 0,
    ) : this(
        date,
        quantity,
        product.id,
        null,
        id
    )

    constructor(
        date: LocalDateTime,
        quantity: Quantity,
        meal: Meal,
        id: Int = 0,
    ) : this(
        date,
        quantity,
        null,
        meal.id,
        id
    )
}
