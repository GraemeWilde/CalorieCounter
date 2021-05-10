package com.wilde.caloriecounter2.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wilde.caloriecounter2.data.food.FoodRepository
import com.wilde.caloriecounter2.data.food.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodViewModel @Inject internal constructor(
    private val foodRepository: FoodRepository,
    //private val savedStateHandle: SavedStateHandle
): ViewModel() {
    val food: Product? = null //TODO("Need to make repository")

    fun insert(food: Product) {
        viewModelScope.launch {
            foodRepository.insert(food)
        }
    }

    fun update(food: Product) {
        viewModelScope.launch {
            foodRepository.update(food)
        }
    }
}