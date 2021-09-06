package com.wilde.caloriecounter2.data.food

import androidx.lifecycle.LiveData
import androidx.room.*
import com.wilde.caloriecounter2.data.food.entities.Product

@Dao
interface FoodDao {
    @Query("SELECT * FROM products")
    suspend fun getAll(): List<Product>

    @Query("SELECT * FROM products")
    fun getAllLive(): LiveData<List<Product>>

    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getByID(id: Int): Product?

    @Query("SELECT * FROM products WHERE off_id = :offId")
    suspend fun getByOffId(offId: String): Product?

    @Insert
    suspend fun insertFoods(vararg foods: Product): List<Long>

    @Update
    suspend fun updateFoods(vararg foods: Product): Int

    @Delete
    suspend fun deleteFoods(vararg foods: Product)
}