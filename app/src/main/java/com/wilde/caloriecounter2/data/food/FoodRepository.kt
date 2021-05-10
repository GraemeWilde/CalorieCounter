package com.wilde.caloriecounter2.data.food

import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodRepository @Inject constructor(
    private val foodDao: FoodDao,
    private val foodWebService: FoodWebService,
) {
    suspend fun get(id: Int): Product? {
        return foodDao.getByID(id)
    }

    suspend fun getAll(): List<Product> {
        return foodDao.getAll()
    }

    suspend fun getByOffId(offId: String): Product? {
        return foodDao.getByOffId(offId)
    }

    suspend fun insert(food: Product) {
        return foodDao.insertFoods(food)
    }

    suspend fun update(food: Product): Int {
        return foodDao.updateFoods(food)
    }

    suspend fun search(searchTerm: String): FoodResponse {
        return foodWebService.search(searchTerm)
    }
}