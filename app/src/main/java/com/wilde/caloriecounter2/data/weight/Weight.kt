package com.wilde.caloriecounter2.data.weight

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "weight")
data class Weight (
    @PrimaryKey val date: LocalDateTime,
    val weight: Float
)