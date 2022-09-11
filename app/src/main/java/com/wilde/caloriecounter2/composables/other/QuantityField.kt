package com.wilde.caloriecounter2.composables.other

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.wilde.caloriecounter2.data.other.quantity.QuantityType

@Composable
fun QuantityField(
    stringValue: String,
    quantityType: QuantityType,
    modifier: Modifier = Modifier,
    onQuantityValueChange: (stringValue: String)->Unit,
    onQuantityTypeChange: (quantityType: QuantityType)->Unit,
    focusRequester: FocusRequester? = null,
    enabledQuantityType: Boolean = true,
) {
    Row {
        val foc = LocalFocusManager.current

        // Measurement Text Field
        TextField(
            label = { Text("Quantity") },
            value = stringValue,
            onValueChange = onQuantityValueChange,
            modifier = (
                    // If focus requester is set use it
                    focusRequester?.let {
                        Modifier.focusRequester(it)
                    } ?: Modifier
                )
                .padding(end = 4.dp)
                .weight(1f)
            ,
            maxLines = 1,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = {
                foc.moveFocus(FocusDirection.Next)
            }),
        )

        // Quantity Type drop down
        EnumDropDownField(
            label = { Text("Quantity Type") },
            //clazz = QuantityType::class.java,
            selectedEnum = quantityType,
            enabled = enabledQuantityType,
            onSelectedChange = onQuantityTypeChange,
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp)
        )
    }
}