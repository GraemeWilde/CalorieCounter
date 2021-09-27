package com.wilde.caloriecounter2.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wilde.caloriecounter2.data.food.entities.Product
import com.wilde.caloriecounter2.data.meals.entities.*
import java.text.DecimalFormat

class MealViewModel : ViewModel() {

    class ObservableMealComponentAndFood(mealComponentAndFood: MealComponentAndFood) {

        class ObservableQuantity(quantity: Quantity) {
            val type = MutableLiveData(quantity.type)
            val measurement = MutableLiveData(quantity.measurement)

            val measurementString = MutableLiveData(
                DecimalFormat("0.#").format(quantity.measurement)
                //quantity.measurement.toString()
            )

            fun measurementOnChange(newMeasurementString: String): Unit {
                if (newMeasurementString.matches(Regex("^(?!$)\\d*(?:\\.\\d+)?"))) {
                    newMeasurementString.toFloatOrNull()?.let {
                        measurement.value = it
                    }
                }
                measurementString.value = newMeasurementString
            }
        }

        val id: MutableLiveData<Int> = MutableLiveData(mealComponentAndFood.mealComponent.id)
        val mealId: MutableLiveData<Int> =
            MutableLiveData(mealComponentAndFood.mealComponent.mealId)
        val foodId: MutableLiveData<Int> =
            MutableLiveData(mealComponentAndFood.mealComponent.foodId)
        val quantity = ObservableQuantity(mealComponentAndFood.mealComponent.quantity)
        val food: MutableLiveData<Product> = MutableLiveData(mealComponentAndFood.food)
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
                    it.mealId.value!!,
                    it.foodId.value!!,
                    Quantity(it.quantity.measurement.value!!, it.quantity.type.value!!)
                )
            } ?: emptyList()
        )
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(this::class.java.name, "onClear")
    }
}