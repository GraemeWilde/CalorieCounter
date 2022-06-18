package com.wilde.caloriecounter2.composables.other

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.wilde.caloriecounter2.data.other.quantity.QuantityType

@Composable
fun Quantity(stringValue: String, quantityType: QuantityType, onValueChange: (stringValue: String, quantityType: QuantityType)->Unit) {
    Row(
        Modifier.padding(top = 4.dp)
    ) {
//        val measurementString by component.quantity.measurementString
//            .observeAsState(" ")

        val foc = LocalFocusManager.current
        // Measurement Text Field
        TextField(
            label = { Text("Measurement") },
            value = stringValue,
            onValueChange = { onValueChange(it, quantityType) },
            modifier = Modifier
                .padding(end = 4.dp)
                .weight(1f)
                //.focusRequester(focusRequester)
            ,
            maxLines = 1,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = {
                foc.moveFocus(FocusDirection.Next)
            })
        )

        //val quantityType by component.quantity.type.observeAsState()

        EnumDropDown(
            label = { Text("Measure Type") },
            //clazz = QuantityType::class.java,
            selectedEnum = quantityType,
            onSelectedChange = {
                onValueChange(stringValue, it as QuantityType)
            },
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp)
        )
    }
}