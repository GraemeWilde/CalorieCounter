package com.wilde.caloriecounter2.data.meals.entities

import androidx.room.ColumnInfo
import com.squareup.moshi.JsonClass

@JsonClass( generateAdapter = true )
data class Quantity(
    @ColumnInfo(name = "measurement")
    var measurement: Float,

    @ColumnInfo(name = "quantity_type")
    var type: QuantityType
)