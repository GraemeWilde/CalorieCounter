package com.wilde.caloriecounter2.data.food.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductJson(
    @Json(name = "brands")
    val brands: String = "",

    @Json(name = "product_name")
    val productName: String = "",

    @Json(name = "id")
    val offId: String = "",

    @Json(name = "code")
    val productCode: String = "",

    @Json(name = "serving_quantity")
    val servingQuantity: Float?,

    @Json(name = "quantity")
    val quantity: String?,

    @Json(name = "nutriments")
    val nutriments: NutrimentsJson?
)