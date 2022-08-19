package com.wilde.caloriecounter2.data.other.quantity

enum class QuantityType {
    Servings,
    GmL {
        override fun toString(): String {
            return "G/mL"
        }
    }
}