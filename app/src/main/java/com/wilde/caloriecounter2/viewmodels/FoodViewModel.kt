package com.wilde.caloriecounter2.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wilde.caloriecounter2.composables.other.NumericField
import com.wilde.caloriecounter2.data.food.FoodRepository
import com.wilde.caloriecounter2.data.food.entities.Nutriments
import com.wilde.caloriecounter2.data.food.entities.PerServing
import com.wilde.caloriecounter2.data.food.entities.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

private const val TAG: String = "FoodViewModel2"

@HiltViewModel
class FoodViewModel @Inject internal constructor(
    private val foodRepository: FoodRepository,
): ViewModel() {

    class ObservableNutriments (nutriments: Nutriments?) {
        val perServing = ObservablePerServing(nutriments?.perServing)
        val per100g = ObservablePerServing(nutriments?.per100g)

        fun toNutriments(): Nutriments {
            return Nutriments(
                perServing.toPerServing(),
                per100g.toPerServing()
            )
        }
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

        fun toPerServing(): PerServing {
            return PerServing(
                servingSize.numeric,
                calories.numeric,
                fat.numeric,
                saturatedFat.numeric,
                transFat.numeric,
                cholesterol.numeric,
                sodium.numeric,
                carbohydrates.numeric,
                fibre.numeric,
                sugars.numeric,
                proteins.numeric
            )
        }
    }

    class ObservableProduct(product: Product? = null) {

        val id = product?.id ?: 0
        val offId = product?.offId ?: ""

        val name = MutableStateFlow<String>(product?.productName ?: "")
        val brands = MutableStateFlow(product?.brands ?: "")
        val code = MutableStateFlow(product?.productCode ?: "")
        val quantity = MutableStateFlow<String>(product?.packageSize ?: "")

        val nutriments = ObservableNutriments(product?.nutriments)

        fun toProduct(): Product {
            return Product(
                id,
                name.value,
                brands.value,
                offId,
                code.value,
                quantity.value,
                nutriments.toNutriments()
            )
        }
    }

    var product: ObservableProduct = ObservableProduct()

    fun openProduct(openProduct: Product) {
        product = ObservableProduct(openProduct)
    }

    fun save() {
        //viewModelScope.launch {
            //withContext(Dispatchers.IO) {
        CoroutineScope(Dispatchers.IO).launch {
            val saveProduct = product.toProduct()
            Log.d(TAG, "Save Started")
            if (saveProduct.id != 0) {
                foodRepository.update(saveProduct)
                //Thread.sleep(5000)
            } else {
                foodRepository.insert(saveProduct)
                //Thread.sleep(5000)
            }
            Log.d(TAG, "Save Completed")
        }
    }
}