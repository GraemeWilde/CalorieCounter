package com.wilde.caloriecounter2.data.other.quantity

import androidx.room.ColumnInfo
import com.squareup.moshi.JsonClass

@JsonClass( generateAdapter = true )
data class Quantity(
    @ColumnInfo(name = "measurement")
    var measurement: Float,

    @ColumnInfo(name = "quantity_type")
    var type: QuantityType
)