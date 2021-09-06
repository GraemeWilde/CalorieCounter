package com.wilde.caloriecounter2.data

import androidx.room.TypeConverter
import com.wilde.caloriecounter2.data.meals.entities.QuantityTypeConverter
import com.wilde.caloriecounter2.data.meals.entities.QuantityTypeConverterInterface
import java.time.LocalDateTime
import java.time.ZoneOffset

class TypeConverters (
    // QuantityTypeConverter from Meal added in to class through composition
    quantityTypeConverter: QuantityTypeConverterInterface = QuantityTypeConverter()
): QuantityTypeConverterInterface by quantityTypeConverter {

    @TypeConverter
    fun localDateTimeToTimestamp(dateTime: LocalDateTime): Long =
        dateTime.toEpochSecond(ZoneOffset.UTC)

    @TypeConverter
    fun timestampToLocalDateTime(timestamp: Long): LocalDateTime =
        LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.UTC)
}