package com.wilde.caloriecounter2.data.food


import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json



/*@Entity(tableName = "food")
data class FoodEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "product_code")
    val productCode: String?,

    @ColumnInfo(name = "product_name")
    val productName: String,

    val serving: Float,
    @ColumnInfo(name = "serving_unit")
    val servingUnit: String,

    val calories: Float,
    val fat: Float,
    val saturatedFat: Float,
    val transFat: Float,
    val cholesterol: Float,
    val sodium: Float,
    val carbohydrate: Float,
    val fibre: Float,
    val sugars: Float,
    val protein: Float,
)*/



/*import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

data class Measure(
    val value: Int,
    val unit: Int
)

@Entity(tableName = "food")
data class FoodEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "product_code")
    val productCode: String?,

    @ColumnInfo(name = "product_name")
    val productName: String,

    @Embedded(prefix = "serving_")
    val serving: Measure,

    @Embedded(prefix = "calories_")
    val calories: Measure,
    @Embedded(prefix = "fat_")
    val fat: Measure,
    @Embedded(prefix = "saturated_fat_")
    val saturatedFat: Measure,
    @Embedded(prefix = "trans_fat_")
    val transFat: Measure,
    @Embedded(prefix = "cholesterol_")
    val cholesterol: Measure,
    @Embedded(prefix = "sodium_")
    val sodium: Measure,
    @Embedded(prefix = "carbohydrate_")
    val carbohydrate: Measure,
    @Embedded(prefix = "fibre_")
    val fibre: Measure,
    @Embedded(prefix = "sugars_")
    val sugars: Measure,
    @Embedded(prefix = "protein_")
    val protein: Measure,
)*/