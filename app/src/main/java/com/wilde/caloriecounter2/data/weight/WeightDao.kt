package com.wilde.caloriecounter2.data.weight

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface WeightDao {
    @Query("SELECT * FROM weight")
    fun getAll(): Flow<List<Weight>>

    @Query("SELECT * FROM weight WHERE date >= :minDate AND date <= :maxDate")
    fun getBetween(minDate: LocalDateTime, maxDate: LocalDateTime): Flow<List<Weight>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg weights: Weight): List<Long>

    @Delete
    suspend fun delete(weight: Weight)
}