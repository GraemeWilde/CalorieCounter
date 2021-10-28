package com.wilde.caloriecounter2.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wilde.caloriecounter2.data.food.FoodRepository
import com.wilde.caloriecounter2.data.food.entities.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FoodListViewModel @Inject internal constructor(
    private val foodRepository: FoodRepository
) : FoodListViewModelClass() {

    private var mutableFoods: LiveData<List<Product>> = MutableLiveData<List<Product>>()

    override val foods: LiveData<List<Product>> by lazy {
        getFoodsList()
        mutableFoods
    }

    private fun getFoodsList() {
        mutableFoods = foodRepository.getAllLive()
    }
}