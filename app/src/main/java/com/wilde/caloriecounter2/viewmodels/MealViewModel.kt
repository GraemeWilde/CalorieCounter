package com.wilde.caloriecounter2.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wilde.caloriecounter2.data.food.entities.Product
import com.wilde.caloriecounter2.data.meals.MealRepository
import com.wilde.caloriecounter2.data.meals.entities.*
import com.wilde.caloriecounter2.data.other.quantity.Quantity
import com.wilde.caloriecounter2.data.other.quantity.QuantityType
import com.wilde.caloriecounter2.viewmodels.helper.ObservableQuantity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
class MealViewModel @Inject internal constructor(
    private val mealRepository: MealRepository
) : ViewModel() {

    class ObservableMealComponentAndFood(food: Product) {

        constructor(mealComponentAndFood: MealComponentAndFood) : this(mealComponentAndFood.food) {
            id.value = mealComponentAndFood.mealComponent.id
            quantity.fromQuantity(mealComponentAndFood.mealComponent.quantity)
            food.value = mealComponentAndFood.food
        }


        val id: MutableLiveData<Int> = MutableLiveData(0)
//        val mealId: MutableLiveData<Int> =
//            MutableLiveData(mealComponentAndFood.mealComponent.mealId)
//        val foodId: MutableLiveData<Int> =
//            MutableLiveData(mealComponentAndFood.mealComponent.foodId)
        val quantity = ObservableQuantity()
        val food: MutableLiveData<Product> = MutableLiveData(food)
    }

    class ObservableMeal(mealAndComponentsAndFoods: MealAndComponentsAndFoods? = null) {
        val id: Int = mealAndComponentsAndFoods?.meal?.id ?: 0
        val name: MutableLiveData<String> = MutableLiveData<String>(mealAndComponentsAndFoods?.meal?.name ?: "")

        val removeList: MutableList<Int> = mutableListOf()

        var observableMealComponentsAndFoods: SnapshotStateList<ObservableMealComponentAndFood> =
            mealAndComponentsAndFoods?.mealComponentsAndFoods?.map {
                ObservableMealComponentAndFood(it)
            }?.toMutableStateList() ?: mutableStateListOf()

        fun toMealAndComponentsAndFoods(): MealAndComponents {
            return MealAndComponents(
                Meal(
                    id,
                    name.value!!
                ),
                observableMealComponentsAndFoods.map {
                    MealComponent(
                        it.id.value!!,
                        id,
                        it.food.value!!.id,
                        Quantity(it.quantity.measurement.value!!, it.quantity.type.value!!)
                    )
                }
            )
        }
    }

    var observableMeal: ObservableMeal = ObservableMeal()

//    val id = MutableLiveData<Int>()
//    val name = MutableLiveData<String>()
//
//
//    var observableMealComponentsAndFoods: SnapshotStateList<ObservableMealComponentAndFood> = mutableStateListOf()

    /*var observableMealComponentsAndFoods: MutableLiveData<MutableList<ObservableMealComponentAndFood>> =
        MutableLiveData<MutableList<ObservableMealComponentAndFood>>(mutableListOf())*/

    fun openMeal(mealAndComponentsAndFoods: MealAndComponentsAndFoods) {
        observableMeal = ObservableMeal(mealAndComponentsAndFoods)
    }

    fun clear() {
        observableMeal = ObservableMeal()
    }

    fun save() {
        val saveMeal = toMealAndComponents()
        val removeList = observableMeal.removeList

        CoroutineScope(Dispatchers.IO).launch {
            Log.d("MealViewModel", "Save Started")

            Log.d("MealViewModel", "RemoveList: ${removeList.toString()}")

            val res = mealRepository.deleteMealComponentsByIds(*removeList.toIntArray())

            Log.d("MealViewModel", "Results: ${res.toString()}")

            if (saveMeal.meal.id != 0) {
                mealRepository.updateMealAndComponents(saveMeal)
            } else {
                mealRepository.insertMealAndComponents(saveMeal)
            }
            Log.d("MealViewModel", "Save Completed")
        }
    }

    fun addComponentAndFood(food: Product) {
        observableMeal.observableMealComponentsAndFoods.add(
            ObservableMealComponentAndFood(
                food
            )
        )
    }

    fun addComponentAndFood(observableMealComponentAndFood: ObservableMealComponentAndFood) {
        observableMeal.observableMealComponentsAndFoods.add(observableMealComponentAndFood)
    }

    fun removeComponentAndFood(observableMealComponentAndFood: ObservableMealComponentAndFood) {
        if (observableMealComponentAndFood.id.value!! != 0) {
            this.observableMeal.removeList.add(observableMealComponentAndFood.id.value!!)
        }
        observableMeal.observableMealComponentsAndFoods.remove(observableMealComponentAndFood)
        /*if (observableMealComponentsAndFoods.value?.remove(observableMealComponentAndFood) == true)
            observableMealComponentsAndFoods.value = observableMealComponentsAndFoods.value*/
    }


    fun toMealAndComponents(): MealAndComponents {
        return observableMeal.toMealAndComponentsAndFoods()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(this::class.java.name, "onClear")
    }
}