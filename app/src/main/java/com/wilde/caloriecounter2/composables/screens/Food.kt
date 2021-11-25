package com.wilde.caloriecounter2.composables.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wilde.caloriecounter2.viewmodels.FoodViewModel

@Preview
@Composable
fun FoodPreview() {
    //val foodViewModel = FoodViewModel2()
    //foodViewModel.openProduct()
    Food(/*foodViewModel = foodViewModel*/)
    //MutableStateFlow
}

/*@Composable
fun FoodView(foodViewModel: FoodViewModel2 = viewModel()) {

}*/

@Composable
fun Food(foodViewModel: FoodViewModel = viewModel()) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .verticalScroll(rememberScrollState()),
        Arrangement.spacedBy(12.dp)
    ) {
        FoodRow {
            val name by foodViewModel.product.name.collectAsState()
            FoodField(
                label = { Text("Name") },
                value = name,
                onValueChange = { foodViewModel.product.name.value = it },
            )
        }

        FoodRow {
            //MutableState
            val brands by foodViewModel.product.brands.collectAsState()
            FoodField(
                label = { Text("Brands") },
                value = brands,
                onValueChange = { foodViewModel.product.brands.value = it },
            )
        }

        FoodRow {
            val code by foodViewModel.product.code.collectAsState()
            FoodField(
                label = { Text("Code") },
                value = code,
                onValueChange = { foodViewModel.product.code.value = it },
            )
        }

        FoodRow {
            val packageSize by foodViewModel.product.quantity.collectAsState()
            val servingSize by foodViewModel.product.nutriments.perServing.servingSize.collectAsState()
            FoodField(
                label = { Text("Package Size") },
                value = packageSize,
                onValueChange = { foodViewModel.product.quantity.value = it },
            )
            FoodField(
                label = { Text("Serving Size") },
                value = servingSize,
                onValueChange = { foodViewModel.product.nutriments.perServing.servingSize.update(it) },
            )
        }

        FoodRow {
            val calories by foodViewModel.product.nutriments.perServing.calories.collectAsState()
            val fat by foodViewModel.product.nutriments.perServing.fat.collectAsState()
            FoodField(
                label = { Text("Calories") },
                value = calories,
                onValueChange = { foodViewModel.product.nutriments.perServing.calories.update(it) },
            )
            FoodField(
                label = { Text("Fat") },
                value = fat,
                onValueChange = { foodViewModel.product.nutriments.perServing.fat.update(it) },
            )
        }

        FoodRow {
            val saturatedFat by foodViewModel.product.nutriments.perServing.saturatedFat.collectAsState()
            val transFat by foodViewModel.product.nutriments.perServing.transFat.collectAsState()
            FoodField(
                label = { Text("Saturated Fat") },
                value = saturatedFat,
                onValueChange = { foodViewModel.product.nutriments.perServing.saturatedFat.update(it) },
            )
            FoodField(
                label = { Text("Trans Fat") },
                value = transFat,
                onValueChange = { foodViewModel.product.nutriments.perServing.transFat.update(it) },
            )
        }

        FoodRow {
            val cholesterol by foodViewModel.product.nutriments.perServing.cholesterol.collectAsState()
            val sodium by foodViewModel.product.nutriments.perServing.sodium.collectAsState()
            FoodField(
                label = { Text("Cholesterol") },
                value = cholesterol,
                onValueChange = { foodViewModel.product.nutriments.perServing.cholesterol.update(it) },
            )
            FoodField(
                label = { Text("Sodium") },
                value = sodium,
                onValueChange = { foodViewModel.product.nutriments.perServing.sodium.update(it) },
            )
        }

        FoodRow {
            val carbohydrates by foodViewModel.product.nutriments.perServing.carbohydrates.collectAsState()
            val fibre by foodViewModel.product.nutriments.perServing.fibre.collectAsState()
            FoodField(
                label = { Text("Carbohydrates") },
                value = carbohydrates,
                onValueChange = {
                    foodViewModel.product.nutriments.perServing.carbohydrates.update(
                        it
                    )
                },
            )
            FoodField(
                label = { Text("Fibre") },
                value = fibre,
                onValueChange = { foodViewModel.product.nutriments.perServing.fibre.update(it) },
            )
        }

        FoodRow {
            val sugars by foodViewModel.product.nutriments.perServing.sugars.collectAsState()
            val proteins by foodViewModel.product.nutriments.perServing.proteins.collectAsState()
            FoodField(
                label = { Text("Sugars") },
                value = sugars,
                onValueChange = { foodViewModel.product.nutriments.perServing.sugars.update(it) },
            )
            FoodField(
                label = { Text("Protein") },
                value = proteins,
                onValueChange = { foodViewModel.product.nutriments.perServing.proteins.update(it) },
            )
        }
    }
}


@Composable
fun FoodRow(
    content: @Composable RowScope.() -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 0.dp),
        Arrangement.spacedBy(12.dp)
    ) {
        content()
    }
}

@Composable
fun RowScope.FoodField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
) {
    val focusManager = LocalFocusManager.current
    TextField(
        value,
        onValueChange,
        modifier
            .padding(horizontal = 0.dp, vertical = 0.dp)
            .weight(1f),
        label = label,
        maxLines = 1,
        keyboardActions = KeyboardActions(onNext = {
            focusManager.moveFocus(FocusDirection.Next)
        }),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
    )
}

/*
fun MyLayout(
    modifier: Modifier = Modifier,
    content: MyLayoutScope.() -> Unit
) {

}

class MyLayoutScope {

    var fields: LinkedHashMap<@Composable () -> Unit, @Composable () -> Unit> = LinkedHashMap()

    fun AddField(
        label: @Composable () -> Unit,
        field: @Composable () -> Unit
    ) {
        fields[label] = field
    }
}*/
