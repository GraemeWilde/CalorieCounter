package com.wilde.caloriecounter2.viewmodels.helper

import androidx.lifecycle.MutableLiveData
import com.wilde.caloriecounter2.data.other.quantity.Quantity
import com.wilde.caloriecounter2.data.other.quantity.QuantityType
import java.text.DecimalFormat

class ObservableQuantity() {
    constructor(quantity: Quantity) : this() {
        type.value = quantity.type
        measurement.value = quantity.measurement

        measurementString.value = DecimalFormat("0.#").format(quantity.measurement)
    }

    val type = MutableLiveData(QuantityType.Ratio)
    val measurement = MutableLiveData(0f)

    val measurementString = MutableLiveData(
        ""
        //DecimalFormat("0.#").format("")
        //quantity.measurement.toString()
    )

    fun fromQuantity(quantity: Quantity) {
        type.value = quantity.type
        measurement.value = quantity.measurement
        measurementString.value = DecimalFormat("0.#").format(quantity.measurement)
    }

    fun measurementOnChange(newMeasurementString: String): Unit {
        if (newMeasurementString.matches(Regex("^(?!$)\\d*(?:\\.\\d+)?"))) {
            newMeasurementString.toFloatOrNull()?.let {
                measurement.value = it
            }
        }
        measurementString.value = newMeasurementString
    }
}