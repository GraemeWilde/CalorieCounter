package com.wilde.caloriecounter2.data

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoField
import java.time.temporal.TemporalField

class TypeConverters {
    @TypeConverter
    fun localDateTimeToTimestamp(dateTime: LocalDateTime): Long =
        dateTime.toEpochSecond(ZoneOffset.UTC)

    @TypeConverter
    fun timestampToLocalDateTime(timestamp: Long): LocalDateTime =
        LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.UTC)
}