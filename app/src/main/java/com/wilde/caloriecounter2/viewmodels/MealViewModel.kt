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

        class ObservableQuantity() {
            constructor(quantity: Quantity) : this() {
                type.value = quantity.type
                measurement.value = quantity.measurement

                measurementString.value = DecimalFormat("0.#").format(quantity.measurement)
            }

            val type = MutableLiveData(QuantityType.Ratio)
            val measurement = MutableLiveData(0f)

            val measurementString = MutableLiveData(
                ""
                //DecimalFormat("0.#").format("")
                //quantity.measurement.toString()
            )

            fun fromQuantity(quantity: Quantity) {
                type.value = quantity.type
                measurement.value = quantity.measurement
                measurementString.value = DecimalFormat("0.#").format(quantity.measurement)
            }

            fun measurementOnChange(newMeasurementString: String): Unit {
                if (newMeasurementString.matches(Regex("^(?!$)\\d*(?:\\.\\d+)?"))) {
                    newMeasurementString.toFloatOrNull()?.let {
                        measurement.value = it
                    }
                }
                measurementString.value = newMeasurementString
            }
        }

        val id: MutableLiveData<Int> = MutableLiveData(0)
//        val mealId: MutableLiveData<Int> =
//            MutableLiveData(mealComponentAndFood.mealComponent.mealId)
//        val foodId: MutableLiveData<Int> =
//            MutableLiveData(mealComponentAndFood.mealComponent.foodId)
        val quantity = ObservableQuantity()
        val food: MutableLiveData<Product> = MutableLiveData(food)
    }

    val id = MutableLiveData<Int>()
    val name = MutableLiveData<String>()


    var observableMealComponentsAndFoods: SnapshotStateList<ObservableMealComponentAndFood> = mutableStateListOf()

    /*var observableMealComponentsAndFoods: MutableLiveData<MutableList<ObservableMealComponentAndFood>> =
        MutableLiveData<MutableList<ObservableMealComponentAndFood>>(mutableListOf())*/

    fun openMeal(mealAndComponentsAndFoods: MealAndComponentsAndFoods) {
        id.value = mealAndComponentsAndFoods.meal.id
        name.value = mealAndComponentsAndFoods.meal.name

        observableMealComponentsAndFoods = mealAndComponentsAndFoods.mealComponentsAndFoods.map {
            ObservableMealComponentAndFood(it)
        }.toMutableStateList()
    }

    fun clear() {
        id.value = 0
        name.value = ""

        observableMealComponentsAndFoods.clear()
    }

    fun save() {
        CoroutineScope(Dispatchers.IO).launch {
            val saveMeal = toMealAndComponents()
            Log.d("MealViewModel", "Save Started")

            if (saveMeal.meal.id != 0) {
                mealRepository.updateMealAndComponents(saveMeal)
            } else {
                mealRepository.insertMealAndComponents(saveMeal)
            }
            Log.d("MealViewModel", "Save Completed")
        }
    }

    fun addComponentAndFood(food: Product) {
        observableMealComponentsAndFoods.add(
            ObservableMealComponentAndFood(
                food
            )
        )
    }

    fun addComponentAndFood(observableMealComponentAndFood: ObservableMealComponentAndFood) {
        observableMealComponentsAndFoods.add(observableMealComponentAndFood)
    }

    fun removeComponentAndFood(observableMealComponentAndFood: ObservableMealComponentAndFood) {
        observableMealComponentsAndFoods.remove(observableMealComponentAndFood)
        /*if (observableMealComponentsAndFoods.value?.remove(observableMealComponentAndFood) == true)
            observableMealComponentsAndFoods.value = observableMealComponentsAndFoods.value*/
    }


    fun toMealAndComponents(): MealAndComponents {
        return MealAndComponents(
            Meal(
                id.value!!,
                name.value!!
            ),
            observableMealComponentsAndFoods.map {
                MealComponent(
                    it.id.value!!,
                    id.value!!,
                    it.food.value!!.id,
                    Quantity(it.quantity.measurement.value!!, it.quantity.type.value!!)
                )
            }
        )
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(this::class.java.name, "onClear")
    }
}