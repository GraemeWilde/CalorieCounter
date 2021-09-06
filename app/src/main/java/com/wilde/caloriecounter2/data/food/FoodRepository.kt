package com.wilde.caloriecounter2.data.food

import androidx.lifecycle.LiveData
import com.wilde.caloriecounter2.data.food.entities.Product
import com.wilde.caloriecounter2.data.food.response.FoodResponse
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

    fun getAllLive(): LiveData<List<Product>> {
        return foodDao.getAllLive()
    }

    suspend fun getAll(): List<Product> {
        return foodDao.getAll()
    }

    suspend fun getByOffId(offId: String): Product? {
        return foodDao.getByOffId(offId)
    }

    suspend fun insert(food: Product): List<Long> {
        return foodDao.insertFoods(food)
    }

    suspend fun update(food: Product): Int {
        return foodDao.updateFoods(food)
    }

    suspend fun search(searchTerm: String): FoodResponse {
        return foodWebService.search(searchTerm)
    }
}