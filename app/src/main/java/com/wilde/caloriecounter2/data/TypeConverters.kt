package com.wilde.caloriecounter2.data

import androidx.room.TypeConverter
import com.wilde.caloriecounter2.data.other.quantity.QuantityTypeConverter
import com.wilde.caloriecounter2.data.other.quantity.QuantityTypeConverterInterface
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

class TypeConverters (
    // QuantityTypeConverter from Meal added in to class through composition
    quantityTypeConverter: QuantityTypeConverterInterface = QuantityTypeConverter()
): QuantityTypeConverterInterface by quantityTypeConverter {

    @TypeConverter
    fun localDateTimeToTimestamp(dateTime: LocalDateTime): Long =
        dateTime.atZone(ZoneId.systemDefault()).toEpochSecond()

    @TypeConverter
    fun timestampToLocalDateTime(timestamp: Long): LocalDateTime =
        Instant.ofEpochSecond(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime()
}