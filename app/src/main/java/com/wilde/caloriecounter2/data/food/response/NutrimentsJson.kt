package com.wilde.caloriecounter2.data.food.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NutrimentsJson(
    @Json(name = "energy-kcal_serving")
    val caloriesServing: Float?,

    @Json(name = "energy-kcal_100g")
    val calories100g: Float?,

    @Json(name = "fat_serving")
    val fatServing: Float?,

    @Json(name = "fat_100g")
    val fat100g: Float?,

    @Json(name = "saturated-fat_serving")
    val saturatedFatServing: Float?,

    @Json(name = "saturated-fat_100g")
    val saturatedFat100g: Float?,

    @Json(name = "trans-fat_serving")
    val transFatServing: Float?,

    @Json(name = "trans-fat_100g")
    val transFat100g: Float?,

    @Json(name = "cholesterol_serving")
    val cholesterolServing: Float?,

    @Json(name = "cholesterol_100g")
    val cholesterol100g: Float?,

    @Json(name = "sodium_serving")
    val sodiumServing: Float?,

    @Json(name = "sodium_100g")
    val sodium100g: Float?,

    @Json(name = "carbohydrates_serving")
    val carbohydratesServing: Float?,

    @Json(name = "carbohydrates_100g")
    val carbohydrates100g: Float?,

    @Json(name = "fiber_serving")
    val fibreServing: Float?,

    @Json(name = "fiber_100g")
    val fibre100g: Float?,

    @Json(name = "sugars_serving")
    val sugarsServing: Float?,

    @Json(name = "sugars_100g")
    val sugars100g: Float?,

    @Json(name = "proteins_serving")
    val proteinsServing: Float?,

    @Json(name = "proteins_100g")
    val proteins100g: Float?,
)