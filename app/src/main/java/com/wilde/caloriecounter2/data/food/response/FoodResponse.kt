package com.wilde.caloriecounter2.data.food.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.wilde.caloriecounter2.data.food.entities.Product

@JsonClass(generateAdapter = true)
data class FoodResponse(
    @Json(name = "count")
    val count: Int = 0,

    @Json(name = "skip")
    val skip: Int = 0,

    @Json(name = "page_size")
    val pageSize: Int = 0,

    @Json(name = "products")
    val products: List<Product>,

    @Json(name = "page")
    val page: Int = 0
)