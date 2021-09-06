package com.wilde.caloriecounter2.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wilde.caloriecounter2.data.food.FoodRepository
import com.wilde.caloriecounter2.data.food.entities.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodViewModel @Inject internal constructor(
    private val foodRepository: FoodRepository,
    //private val savedStateHandle: SavedStateHandle
): ViewModel() {
    lateinit var savingFood: Product

    private val mutableSaving = MutableLiveData<Boolean>(false)
    val saving: LiveData<Boolean> get() = mutableSaving

    private val mutableAskOverwrite = MutableLiveData<Boolean>(false)
    val askOverwrite: LiveData<Boolean> get() = mutableAskOverwrite

    var saveSuccessful: (() -> Unit)? = null

    enum class Overwrite {
        Overwrite,
        NewRecord,
        Prompt
    }

    fun save(food: Product, overwrite: Overwrite = Overwrite.Prompt) {
        mutableSaving.value = true
        viewModelScope.launch {
            if (food.id != 0) {
                // If product has an id it means you are editing an existing record
                foodRepository.update(food)
            } else if (overwrite == Overwrite.NewRecord) {
                // If you want a new record
                foodRepository.insert(food)
            } else {
                if (food.offId != "") {
                    val otherProd = foodRepository.getByOffId(food.offId)
                    if (otherProd != null) {
                        if (overwrite == Overwrite.Overwrite) {
                            val prod = food.copy(id = otherProd.id)
                            foodRepository.update(prod)
                            mutableSaving.value = false
                            saveSuccessful?.invoke()
                        } else {
                            savingFood = food
                            mutableAskOverwrite.value = true
                        }
                        // Either overwrite was selected, or flag variable to have fragment ask
                        // user if they want to overwrite was set, return
                        return@launch
                    }
                }
                // If product does not have an offId, or there isn't a matching record in the
                // database
                foodRepository.insert(food)
            }
            mutableSaving.value = false
            saveSuccessful?.invoke()
        }
    }

    private fun saveFinished() {
        mutableSaving.value = false
        saveSuccessful?.invoke()
    }

    fun getByOffId(offId: String) {
        viewModelScope.launch {
            foodRepository.getByOffId(offId)
        }
    }

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