package com.wilde.caloriecounter2.data.meals.entities

import androidx.room.ColumnInfo

data class Quantity(
    @ColumnInfo(name = "measurement")
    val measurement: Float,

    @ColumnInfo(name = "quantity_type")
    val type: QuantityType
)