package com.wilde.caloriecounter2.data.food.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class PerServing(
    @ColumnInfo(name = "serving_size")
    val servingSize: Float?,

    @ColumnInfo(name = "calories")
    val calories: Float?,

    @ColumnInfo(name = "fat")
    val fat: Float?,

    @ColumnInfo(name = "saturated_fat")
    val saturatedFat: Float?,

    @ColumnInfo(name = "trans_fat")
    val transFat: Float?,

    @ColumnInfo(name = "cholesterol")
    val cholesterol: Float?,

    @ColumnInfo(name = "sodium")
    val sodium: Float?,

    @ColumnInfo(name = "carbohydrates")
    val carbohydrates: Float?,

    @ColumnInfo(name = "fibre")
    val fibre: Float?,

    @ColumnInfo(name = "sugars")
    val sugars: Float?,

    @ColumnInfo(name = "proteins")
    val proteins: Float?,
) : Parcelable