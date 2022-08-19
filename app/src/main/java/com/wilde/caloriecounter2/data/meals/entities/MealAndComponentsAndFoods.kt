package com.wilde.caloriecounter2.data.meals.entities

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Relation
import com.squareup.moshi.JsonClass
import com.wilde.caloriecounter2.data.other.quantity.QuantityType
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass( generateAdapter = true )
data class MealAndComponentsAndFoods(
    @Embedded
    val meal: Meal,

    @Relation(
        entity = MealComponent::class,
        entityColumn = "meal_id",
        parentColumn = "id"
    ) val mealComponentsAndFoods: List<MealComponentAndFood>
) : Parcelable {
    @Ignore
    val calorieSum: Float
    @Ignore
    val proteinsSum: Float
    @Ignore
    val fatSum: Float
    @Ignore
    val carbohydratesSum: Float

    init {
        var tCalorieSum: Float = 0f
        var tProteinsSum: Float = 0f
        var tFatSum: Float = 0f
        var tCarbohydratesSum: Float = 0f

        mealComponentsAndFoods.forEach {
            it.food.nutriments?.perServing?.let { perServing ->
                it.mealComponent.quantity.let { quantity ->
                    when (quantity.type) {
                        QuantityType.Servings -> {
                            tCalorieSum += quantity.measurement * (perServing.calories ?: 0f)
                            tProteinsSum += quantity.measurement * (perServing.proteins ?: 0f)
                            tFatSum += quantity.measurement * (perServing.fat ?: 0f)
                            tCarbohydratesSum += quantity.measurement * (perServing.carbohydrates ?: 0f)
                        }
                        QuantityType.GmL -> {
                            if (perServing.servingSize != null) {
                                tCalorieSum += (perServing.calories ?: 0f) / perServing.servingSize * quantity.measurement
                                tProteinsSum += (perServing.proteins ?: 0f) / perServing.servingSize * quantity.measurement
                                tFatSum += (perServing.fat ?: 0f) / perServing.servingSize * quantity.measurement
                                tCarbohydratesSum += (perServing.carbohydrates ?: 0f) / perServing.servingSize * quantity.measurement
                            }
                        }
                    }
                }
            }
        }
        calorieSum = tCalorieSum
        proteinsSum = tProteinsSum
        fatSum = tFatSum
        carbohydratesSum = tCarbohydratesSum
    }
}
