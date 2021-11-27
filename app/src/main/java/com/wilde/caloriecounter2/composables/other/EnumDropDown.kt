package com.wilde.caloriecounter2.composables.other

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

/**
 * Exposed Dropdown field that displays selectable values from a passed in enum class
 *
 * @param clazz the enum to use for the values
 * @param selectedEnum the selected value to be shown
 * @param modifier a [Modifier] for this EnumDropDown
 * @param labal a label for this EnumDropDown
 * @param onSelectedChange the callback that is triggered when a new enum is selected
 */
@Composable
fun <T : Enum<T>> EnumDropDown(
    clazz: Class<T>,
    selectedEnum: T,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    onSelectedChange: (select: T) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    @OptIn(ExperimentalMaterialApi::class)
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
        modifier = Modifier.width(IntrinsicSize.Min).then(modifier)
    ) {
        TextField(
            value = selectedEnum.name,
            label = label,
            onValueChange = { },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Icon(
                    imageVector =
                    if (expanded)
                        Icons.Filled.ArrowDropUp
                    else
                        Icons.Filled.ArrowDropDown,
                    contentDescription = null
                )
            },
            readOnly = true,
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            clazz.enumConstants!!.forEach {
                DropdownMenuItem(onClick = {
                    onSelectedChange(it)
                    expanded = false
                }) {
                    Text(it.name)
                }
            }
        }
    }
}