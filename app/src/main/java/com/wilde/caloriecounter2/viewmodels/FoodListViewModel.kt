package com.wilde.caloriecounter2.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.moshi.Types
import com.wilde.caloriecounter2.data.food.FoodRepository
import com.wilde.caloriecounter2.data.food.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodListViewModel @Inject internal constructor(
    private val foodRepository: FoodRepository,
    //private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val foods: MutableLiveData<List<Product>> by lazy {
        MutableLiveData<List<Product>>()
    }

    fun search(searchTerm: String) {
        viewModelScope.launch {
            val response = foodRepository.search(searchTerm)
            val getProd = foodRepository.getByOffId(response.products[0].offId)
            if (getProd != null) {
                val prod = response.products[0].copy(id = getProd.id)
                val ret = foodRepository.update(prod)//response.products[0])
            } else {
                val ret = foodRepository.insert(response.products[0])
            }

            val test = foodRepository.getAll()
            Log.d("foodListVM", "Search")

            foods.value = response.products
        }
    }
}