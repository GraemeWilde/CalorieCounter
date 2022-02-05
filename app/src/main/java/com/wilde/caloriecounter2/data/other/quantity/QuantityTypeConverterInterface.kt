package com.wilde.caloriecounter2.data.other.quantity

import androidx.room.TypeConverter

interface QuantityTypeConverterInterface {
    @TypeConverter
    fun quantityTypeToInt(quantityType: QuantityType): Int

    @TypeConverter
    fun intToQuantityType(quantityTypeOrdinal: Int): QuantityType
}