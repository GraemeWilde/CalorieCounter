package com.wilde.caloriecounter2.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wilde.caloriecounter2.data.food.FoodRepository
import com.wilde.caloriecounter2.data.food.response.FoodResponse
import com.wilde.caloriecounter2.data.food.entities.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodSearchViewModel @Inject internal constructor(
    private val foodRepository: FoodRepository,
    //private val savedStateHandle: SavedStateHandle
) : FoodListViewModelClass() {

    private var response: FoodResponse? = null
    private val mutableQueryText = MutableLiveData<String>()
    val queryText: LiveData<String> get() = mutableQueryText

    private val mutableSearching = MutableLiveData<Boolean>()
    val searching: LiveData<Boolean> get() = mutableSearching

    override val foods: MutableLiveData<List<Product>> by lazy {
        MutableLiveData<List<Product>>()
    }

    fun search(searchTerm: String) {
        mutableQueryText.value = searchTerm
        foods.value = emptyList()
        mutableSearching.value = true

        viewModelScope.launch {
            val response = foodRepository.search(searchTerm)

            /*val getProd = foodRepository.getByOffId(response.products[0].offId)
            if (getProd != null) {
                val prod = response.products[0].copy(id = getProd.id)
                val ret = foodRepository.update(prod)//response.products[0])
            } else {
                val ret = foodRepository.insert(response.products[0])
            }

            val test = foodRepository.getAll()
            Log.d("foodListVM", "Search")*/


            foods.value = response.products
            this@FoodSearchViewModel.response = response
            mutableSearching.value = false
        }
    }
}