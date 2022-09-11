package com.wilde.caloriecounter2.other

import com.wilde.caloriecounter2.data.meals.entities.MealAndComponentsAndFoods
import com.wilde.caloriecounter2.data.meals.entities.MealComponentAndFood
import com.wilde.caloriecounter2.data.other.quantity.Quantity
import com.wilde.caloriecounter2.data.other.quantity.QuantityType

class Macros(calories: Float, proteins: Float, fat: Float, carbohydrates: Float, measurement: Float, quantityType: QuantityType, servingSize: Float? = null) {

    val calorieSum: Float?
    val proteinsSum: Float?
    val fatSum: Float?
    val carbohydratesSum: Float?

    init {

        when (quantityType) {
            QuantityType.Servings -> {
                calorieSum = measurement * (calories)
                proteinsSum = measurement * (proteins ?: 0f)
                fatSum = measurement * (fat ?: 0f)
                carbohydratesSum = measurement * (carbohydrates ?: 0f)
            }
            QuantityType.GmL -> {
                if (servingSize != null) {
                    calorieSum = (calories ?: 0f) / servingSize * measurement
                    proteinsSum = (proteins ?: 0f) / servingSize * measurement
                    fatSum = (fat ?: 0f) / servingSize * measurement
                    carbohydratesSum = (carbohydrates ?: 0f) / servingSize * measurement
                } else {
                    calorieSum = null
                    proteinsSum = null
                    fatSum = null
                    carbohydratesSum = null
                }
            }
        }
    }
}