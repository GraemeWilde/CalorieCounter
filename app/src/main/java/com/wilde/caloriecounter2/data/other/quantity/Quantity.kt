package com.wilde.caloriecounter2.data.other.quantity

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass( generateAdapter = true )
data class Quantity(
    @ColumnInfo(name = "measurement")
    var measurement: Float,

    @ColumnInfo(name = "quantity_type")
    var type: QuantityType
) : Parcelable