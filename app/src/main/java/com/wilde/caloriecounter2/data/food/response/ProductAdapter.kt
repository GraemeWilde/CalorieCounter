package com.wilde.caloriecounter2.data.food.response

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import com.wilde.caloriecounter2.data.food.entities.Nutriments
import com.wilde.caloriecounter2.data.food.entities.PerServing
import com.wilde.caloriecounter2.data.food.entities.Product

class ProductAdapter {
    @ToJson
    fun toJson(product: Product): ProductJson {
        val perServing = product.nutriments?.perServing
        val p100g = product.nutriments?.per100g
        val nutriment = NutrimentsJson(
            perServing?.calories,
            p100g?.calories,
            perServing?.fat,
            p100g?.fat,
            perServing?.saturatedFat,
            p100g?.saturatedFat,
            perServing?.transFat,
            p100g?.transFat,
            perServing?.cholesterol,
            p100g?.cholesterol,
            perServing?.sodium,
            p100g?.sodium,
            perServing?.carbohydrates,
            p100g?.carbohydrates,
            perServing?.fibre,
            p100g?.fibre,
            perServing?.sugars,
            p100g?.sugars,
            perServing?.proteins,
            p100g?.proteins
        )
        return ProductJson(
            product.brands,
            product.productName,
            product.offId,
            product.productCode,
            p100g?.servingSize,
            product.packageSize,
            nutriment
        )
    }

    @FromJson
    fun fromJson(productJson: ProductJson): Product {
        val nutriments = productJson.nutriments?.let { nutrimentJson ->
            val perServing = if (productJson.servingQuantity == null) null
            else with(nutrimentJson) {
                PerServing(
                    productJson.servingQuantity,
                    caloriesServing,
                    fatServing,
                    saturatedFatServing,
                    transFatServing,
                    cholesterolServing,
                    sodiumServing,
                    carbohydratesServing,
                    fibreServing,
                    sugarsServing,
                    proteinsServing
                )
            }

            val per100g = if (nutrimentJson.calories100g == null) null
            else with(nutrimentJson) {
                PerServing(
                    100f,
                    calories100g,
                    fat100g,
                    saturatedFat100g,
                    transFat100g,
                    cholesterol100g,
                    sodium100g,
                    carbohydrates100g,
                    fibre100g,
                    sugars100g,
                    proteins100g
                )
            }


            Nutriments(perServing, per100g)
        }

        return Product(
            0,
            productJson.brands,
            productJson.productName,
            productJson.offId,
            productJson.productCode,
            productJson.quantity,
            nutriments
        )
    }
}