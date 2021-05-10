package com.wilde.caloriecounter2.data.weight

import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeightRepository @Inject constructor(
    private val weightDao: WeightDao
) {
    fun getAll(): Flow<List<Weight>> {
        return weightDao.getAll()
    }

    suspend fun addWeight(date: LocalDateTime, weight: Float): List<Long> {
        return weightDao.insertAll(Weight(date, weight))
    }
}