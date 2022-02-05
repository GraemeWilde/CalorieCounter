package com.wilde.caloriecounter2.data.other.quantity


class QuantityTypeConverter() : QuantityTypeConverterInterface {
    private val quantityEnumValues by lazy {
        QuantityType.values()
    }

    override fun quantityTypeToInt(quantityType: QuantityType): Int =
        quantityType.ordinal

    override fun intToQuantityType(quantityTypeOrdinal: Int): QuantityType =
        quantityEnumValues[quantityTypeOrdinal]
}