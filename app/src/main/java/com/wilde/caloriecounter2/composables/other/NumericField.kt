package com.wilde.caloriecounter2.composables.other

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


private fun String.checkNumeric(): Boolean {
    return this.matches(Regex("^(?!$)\\d*(?:\\.\\d+)?"))
}

class NumericField(value: Float? = null) {
    private var _numeric: Float? = value
    val numeric: Float? get() = _numeric
    private var _string = MutableStateFlow(value.toString())
    val string: StateFlow<String> get() = _string


    fun update(newVal: String) {
        _string.value = newVal

        if (newVal.checkNumeric()) {
            newVal.toFloatOrNull()?.let {
                _numeric = it
                return
            }
        }

        //else
        _numeric = null
    }

    @Composable
    fun collectAsState(): State<String> {
        return string.collectAsState()
    }
}