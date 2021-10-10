package com.wilde.caloriecounter2.composables.other

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity

@Composable
fun <T : Enum<T>> EnumDropDown(
    clazz: Class<T>,
    selectedEnum: T,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    onSelectedChange: (select: T) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val tfColors = (@Composable {
        val textFieldDefaults = TextFieldDefaults.textFieldColors()
        TextFieldDefaults.textFieldColors(
            disabledTextColor = textFieldDefaults.textColor(enabled = true).value,
            disabledTrailingIconColor = textFieldDefaults.trailingIconColor(
                enabled = true,
                isError = false
            ).value,
            backgroundColor = textFieldDefaults.backgroundColor(enabled = true).value,
            disabledLabelColor = MaterialTheme.colors.onSurface.copy(ContentAlpha.medium),
        )
    })()

    /*val tfColors = TextFieldDefaults.textFieldColors(
        disabledTextColor = TextFieldDefaults
            .outlinedTextFieldColors().textColor(enabled = true).value,
        disabledTrailingIconColor = TextFieldDefaults
            .outlinedTextFieldColors().trailingIconColor(
                enabled = true,
                isError = false
            ).value,
        //backgroundColor = Color.Transparent,
        backgroundColor = TextFieldDefaults
            .outlinedTextFieldColors().backgroundColor(enabled = true).value,
        disabledLabelColor = MaterialTheme.colors.onSurface.copy(ContentAlpha.medium),
    )*/

    /*val tfColors = TextFieldDefaults.textFieldColors(
        disabledTextColor =
    )*/

    //MaterialTheme.colors.onSurface.copy(ContentAlpha.medium)
    var dropDownWidth by remember { mutableStateOf(0) }
    //var dropDownSize by remember { mutableStateOf(Size.Zero) }
    //var dropDownPos by remember { mutableStateOf(Offset.Zero) }

    Box(modifier = modifier) {
        TextField(
            value = selectedEnum.name,
            label = label,
            onValueChange = { },
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
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    expanded = true
                }
                .onSizeChanged {
                    dropDownWidth = it.width
                },
            /*.onGloballyPositioned {
                dropDownPos = it.positionInWindow()
            }*/
            enabled = false,
            colors = tfColors,
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(
                with(LocalDensity.current) {
/*                        Log.d("Width", dropDownWidth.toDp().toString())
                        Log.d(
                            "Position",
                            "${dropDownPos.x.toDp().toString()} - ${
                                dropDownPos.y.toDp().toString()
                            }"
                        )*/
                    dropDownWidth.toDp()
                })
        ) {
            //T::class.java.enumConstants.forEach {  }
            //val clazz: Class<T> = T::class.java

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