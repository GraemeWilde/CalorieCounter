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

    constructor(quantity: ObservableQuantity) : this() {
        type.value = quantity.type.value
        measurement.value = quantity.measurement.value
        measurementString.value = quantity.measurementString.value
    }

    val type = MutableLiveData(QuantityType.Servings)
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

    fun toQuantity(): Quantity? {
        val meas = measurement.value
        val typ = type.value

        return if (meas != null && typ != null) {
            Quantity(meas, typ)
        } else {
            null
        }
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