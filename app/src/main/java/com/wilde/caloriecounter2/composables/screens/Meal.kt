package com.wilde.caloriecounter2.composables.screens

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wilde.caloriecounter2.composables.other.EnumDropDown
import com.wilde.caloriecounter2.composables.other.scrollbarVertical
import com.wilde.caloriecounter2.data.food.entities.Product
import com.wilde.caloriecounter2.data.meals.entities.*
import com.wilde.caloriecounter2.viewmodels.MealViewModel


private val primary = Color(0xFF9C0000)
//private val primaryLight = Color(0xFFD5442B)
private val primaryDark = Color(0xFF670000)

private val black = Color(0xFFFFFFFF)
private val white = Color(0xFF000000)

private val darkColors = darkColors(
    primary = primary,
    primaryVariant = primaryDark,
    onPrimary = white,
    background = black,
    onBackground = white
)

private val lightColors = lightColors(
    primary = primary,
    primaryVariant = primaryDark,
    onPrimary = white,
    background = white,
    onBackground = black
)


@Preview(device = Devices.PIXEL_4, showSystemUi = true)
@Composable
fun MealViewPreview() {
    val meal = MealAndComponentsAndFoods(
        Meal(
            name = "Test Meal"
        ),
        listOf(
            MealComponentAndFood(
                MealComponent(
                    quantity = Quantity(
                        1f,
                        QuantityType.Ratio
                    )
                ),
                Product(
                    productName = "Egg Whites",
                    brands = "Burnbrae Farms Naturegg"
                )
            )
        )
    )
    val viewModel = MealViewModel()
    viewModel.openMeal(meal)
    MealView(viewModel)
}


@Composable
fun MealView(mealViewModel: MealViewModel = viewModel()) {
    /*val (colors, typography, shapes) = createMdcTheme(
        LocalContext.current,
        LocalLayoutDirection.current
    )*/

    MaterialTheme(
        colors = if (isSystemInDarkTheme()) darkColors else lightColors,
        typography = Typography(),
        shapes = Shapes()
        /*colors = colors!!,
        typography = typography!!,
        shapes = shapes!!*/
    ) {
        var selectingFood by remember { mutableStateOf(false) }

        if (selectingFood)
            FoodList {
                mealViewModel.addComponentAndFood(it)
                //MealViewModel.ObservableMealComponentAndFood()
                selectingFood = false
            }
        else
            MealViewComponent(mealViewModel) {
                selectingFood = true
            }
    }
}

@Composable
fun MealViewComponent(viewModel: MealViewModel, onAddComponent: () -> Unit) {
    Box {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            val mealName: String by viewModel.name.observeAsState(" ")
            TextField(
                label = { Text("Meal Name") },
                value = mealName,
                onValueChange = {
                    viewModel.name.value = it
                },
                modifier = Modifier.fillMaxWidth(),
            )

            val mealComponentsAndFoods =
                remember { viewModel.observableMealComponentsAndFoods }

            ComponentsList(mealComponentsAndFoods, viewModel::removeComponentAndFood)
        }
        FloatingActionButton(
            onClick = onAddComponent,
            Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Filled.Add, null)
        }
    }
}

@Composable
fun ComponentsList(
    mealComponentsAndFoods: MutableList<MealViewModel.ObservableMealComponentAndFood>?,
    onRemoveComponent: (component: MealViewModel.ObservableMealComponentAndFood) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        Modifier
            .verticalScroll(scrollState)
            //.scrollbarVertical(lazyListState)
            .scrollbarVertical(scrollState = scrollState)
        ,
        //.scrollbarVertical(scrollState = scrollState)
    ) {
        //
        /*if (mealComponentsAndFoods != null) {
            for (i in 0..6) {
                itemsIndexed(mealComponentsAndFoods.toList()) { index, component ->
                    Box(
                        *//*Modifier.onSizeChanged {
                                Log.d("SizeChanged", "${i * 2 + index} - ${it.height.toString()}")
                            }*//*
                        ) {
                            */
        mealComponentsAndFoods?.forEach { component ->
            Card(
                border = BorderStroke(1.dp, Color(0x66000000)),
                elevation = 4.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Column(
                    Modifier.padding(8.dp)
                ) {
                    Row(
                        Modifier.height(IntrinsicSize.Min)
                    ) {
                        Column(
                            Modifier.weight(1f)
                        ) {
                            Text(
                                text = component.food.value!!.productName,
                                fontWeight = FontWeight.Bold
                            )
                            Text(text = component.food.value!!.brands)
                        }

                        // Remove this ComponentAndFood button
                        IconButton(
                            onClick = {
                                onRemoveComponent(component)
                            },
                            Modifier.then(
                                Modifier
                                    //.fillMaxHeight()
                                    //.aspectRatio(1f)
                                    .size(42.dp)
                            )
                        ) {
                            Icon(
                                Icons.Filled.Delete,
                                null,
                                Modifier
                                    .fillMaxHeight()
                                    .aspectRatio(1f)
                            )
                        }
                    }
                    Row(
                        Modifier.padding(top = 4.dp)
                    ) {
                        val measurementString by component.quantity.measurementString
                            .observeAsState("0")

                        TextField(
                            label = { Text("Measurement") },
                            value = measurementString,
                            onValueChange = component.quantity::measurementOnChange,
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .weight(1f)
                        )

                        val quantityType by component.quantity.type.observeAsState()

                        EnumDropDown(
                            label = { Text("Measure Type") },
                            clazz = QuantityType::class.java,
                            selectedEnum = quantityType!!,
                            onSelectedChange = {
                                component.quantity.type.value = it
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 4.dp)
                        )
                    }
                }
            }
        }
    }
}



