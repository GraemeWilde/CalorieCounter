package com.wilde.caloriecounter2.viewmodels

import androidx.lifecycle.ViewModel
import com.wilde.caloriecounter2.composables.other.NumericField
import com.wilde.caloriecounter2.data.food.entities.Nutriments
import com.wilde.caloriecounter2.data.food.entities.PerServing
import com.wilde.caloriecounter2.data.food.entities.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class FoodViewModel2 @Inject internal constructor(): ViewModel() {

    class ObservableNutriments (nutriments: Nutriments?) {
        val perServing = ObservablePerServing(nutriments?.perServing)
        val per100g = ObservablePerServing(nutriments?.per100g)
    }

    class ObservablePerServing (perServing: PerServing?) {
        val servingSize = NumericField(perServing?.servingSize)
        val calories = NumericField(perServing?.calories)
        val fat = NumericField(perServing?.fat)
        val saturatedFat = NumericField(perServing?.saturatedFat)
        val transFat = NumericField(perServing?.transFat)
        val cholesterol = NumericField(perServing?.cholesterol)
        val sodium = NumericField(perServing?.sodium)
        val carbohydrates = NumericField(perServing?.carbohydrates)
        val fibre = NumericField(perServing?.fibre)
        val sugars = NumericField(perServing?.sugars)
        val proteins = NumericField(perServing?.proteins)
    }

    class ObservableProduct(product: Product? = null) {
        val name = MutableStateFlow<String>(product?.productName ?: "")
        val brands = MutableStateFlow(product?.brands ?: "")
        val code = MutableStateFlow(product?.productCode ?: "")
        val quantity = MutableStateFlow<String>(product?.packageSize ?: "")

        val nutriments = ObservableNutriments(product?.nutriments)
    }

    var product: ObservableProduct = ObservableProduct()

    fun openProduct(openProduct: Product) {
        product = ObservableProduct(openProduct)
    }

    fun save() {

    }
}