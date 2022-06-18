package com.wilde.caloriecounter2.data.food.entities

import android.os.Parcelable
import androidx.room.Embedded
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Nutriments(
    @Embedded(prefix = "per_serving_")
    val perServing: PerServing?,
    @Embedded(prefix = "per_100g_")
    val per100g: PerServing?,
) : Parcelable