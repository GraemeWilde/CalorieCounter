package com.wilde.caloriecounter2.composables.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wilde.caloriecounter2.composables.other.*
import com.wilde.caloriecounter2.composables.other.TextField
import com.wilde.caloriecounter2.data.food.entities.Product
import com.wilde.caloriecounter2.data.meals.entities.*
import com.wilde.caloriecounter2.data.other.quantity.Quantity
import com.wilde.caloriecounter2.data.other.quantity.QuantityType
import com.wilde.caloriecounter2.viewmodels.FoodListViewModel
import com.wilde.caloriecounter2.viewmodels.MealViewModel
import kotlinx.coroutines.android.awaitFrame


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
                        QuantityType.Servings
                    )
                ),
                Product(
                    productName = "Egg Whites",
                    brands = "Burnbrae Farms Naturegg"
                )
            )
        )
    )
    val viewModel: MealViewModel = hiltViewModel()
    viewModel.openMeal(meal)
    MealScreen(viewModel)
}


@Composable
fun MealScreen(
    mealViewModel: MealViewModel = viewModel(),
    //onAddComponent: () -> Unit
) {
    /*val (colors, typography, shapes) = createMdcTheme(
        LocalContext.current,
        LocalLayoutDirection.current
    )*/

    //MealViewComponent(mealViewModel, onAddComponent)

    var selectingFood by remember { mutableStateOf(false) }

//    val callback = remember {
//
//    }

    val foodListViewModel: FoodListViewModel = hiltViewModel()

    RunOnce {
        foodListViewModel.getFoodsList()
    }


    if (selectingFood) {

        DialogFix(
            onDismissRequest = {
                Log.d("Meal", "Dismiss")
                selectingFood = false
            }
        ) {
            Surface(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                FoodList(foodListViewModel) {
                    mealViewModel.addComponentAndFood(it)
                    //MealViewModel.ObservableMealComponentAndFood()
                    Log.d("Meal", "Select Food: ${it.productName}")
                    selectingFood = false
                }
            }
        }
    }

    Box(
        Modifier.fillMaxSize()
    ) {
        Meal(mealViewModel)

        SlideInFloatingActionButton { selectingFood = true }
//        val state = remember {
//            MutableTransitionState(false).apply {
//                targetState = true
//            }
//        }
//        AnimatedVisibility(
//            visibleState = state,
//            enter = slideInVertically(tween(500)) { it },
//            exit = slideOutVertically(tween(500)) { it },
//            modifier = Modifier
//                .align(Alignment.BottomEnd)
//                .padding(16.dp)
//        ) {
//            FloatingActionButton(
//                onClick = { selectingFood = true },
//            ) {
//                Icon(Icons.Filled.Add, null)
//            }
//        }
    }
}

@Composable
fun Meal(viewModel: MealViewModel) {

    Column(
        modifier = Modifier
            .padding(8.dp)
    ) {
        val foc = LocalFocusManager.current
        val mealName: String by viewModel.observableMeal.name.distinctUntilChanged().observeAsState(" ")
        val titleFocusRequester = remember { FocusRequester() }

        TextField(
            label = { Text("Meal Name") },
            value = mealName,
            onValueChange = {
                viewModel.observableMeal.name.value = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(titleFocusRequester),
            maxLines = 1,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = {
                foc.moveFocus(FocusDirection.Next)
            })
        )

        if (viewModel.observableMeal.new) {
            LaunchedEffect(true) {
                titleFocusRequester.requestFocus()
                viewModel.observableMeal.new = false
            }
        }

//            val mealComponentsAndFoods =
//                remember { viewModel.observableMealComponentsAndFoods }

        ComponentsList(
            viewModel.observableMeal.observableMealComponentsAndFoods,
            viewModel::removeComponentAndFood
        )
    }
}

@Composable
fun ComponentsList(
    mealComponentsAndFoods: SnapshotStateList<MealViewModel.ObservableMealComponentAndFood>?,
    onRemoveComponent: (component: MealViewModel.ObservableMealComponentAndFood) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        Modifier
            .verticalScroll(scrollState),
        //.scrollbarVertical(lazyListState)
        //.scrollbarVertical(scrollState = scrollState)
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
        var firstLaunch by remember { mutableStateOf(true) }
//        val noRecompFirstLaunch by rememberUpdatedState(firstLaunch)
//        val lastFocus = remember { FocusRequester() }

        mealComponentsAndFoods?.forEach { component ->
            val focusRequester = remember { FocusRequester() }
            val new by remember { mutableStateOf(!firstLaunch) }

            Log.d("Meal", "${component.food.value!!.productName} - $new")

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
                        Modifier.height(IntrinsicSize.Min),
                        verticalAlignment = Alignment.CenterVertically
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
                        val onRem = remember { { onRemoveComponent(component) } }
                        DeleteButton(onRem)
                        /*// Remove this ComponentAndFood button
                        Card(
                            border = BorderStroke(1.dp, Color(0x66000000)),
                            elevation = 4.dp,
                            backgroundColor = Color(0xFFB00020) //0xFF800000)
                        ) {
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
                                        .fillMaxHeight(0.9f)
                                        .aspectRatio(1f)
                                )
                            }
                        }*/
                    }

                    val measurementString by component.quantity.measurementString.observeAsState(" ")
                    val quantityType by component.quantity.type.observeAsState(QuantityType.Servings)

                    QuantityField(
                        stringValue = measurementString,
                        quantityType = quantityType,
                        onQuantityValueChange = component.quantity::measurementOnChange,
                        onQuantityTypeChange = { component.quantity.type.value = it },
                        focusRequester = focusRequester
                    )

//                    Row(
//                        Modifier.padding(top = 0.dp)
//                    ) {
//                        val measurementString by component.quantity.measurementString
//                            .observeAsState(" ")
//
//                        val foc = LocalFocusManager.current
//                        // Measurement Text Field
//                        TextField(
//                            label = { Text("Measurement") },
//                            value = measurementString,
//                            onValueChange = component.quantity::measurementOnChange,
//                            modifier = Modifier
//                                .padding(end = 4.dp)
//                                .weight(1f)
//                                .focusRequester(focusRequester),
//                            maxLines = 1,
//                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
//                            keyboardActions = KeyboardActions(onNext = {
//                                foc.moveFocus(FocusDirection.Next)
//                            })
//                        )
//
//                        val quantityType by component.quantity.type.observeAsState()
//
//                        EnumDropDown(
//                            label = { Text("Measure Type") },
//                            //clazz = QuantityType::class.java,
//                            selectedEnum = quantityType!!,
//                            onSelectedChange = {
//                                component.quantity.type.value = it
//                            },
//                            modifier = Modifier
//                                .weight(1f)
//                                .padding(start = 4.dp)
//                        )
//                    }
                }
            }

            if (new) {
//                @OptIn(ExperimentalComposeUiApi::class)
//                val key = LocalSoftwareKeyboardController.current
                /*LocalView.current.viewTreeObserver.addOnWindowFocusChangeListener {
                    focusRequester.requestFocus()
                }*/

                LaunchedEffect(true) {
                    awaitFrame()
                    focusRequester.requestFocus()
//                    withContext(Dispatchers.IO) {
//                        //@OptIn(ExperimentalComposeUiApi::class)
//                        //key!!.show()
//                        Thread.sleep(4000)
//                        withContext(Dispatchers.Main) {
//                            focusRequester.requestFocus()
//                        }
//                    }
                }
            }
        }

        RunOnce {
            firstLaunch = false
        }
    }
}

@Composable
fun ComponentDeleteButton(component: MealViewModel.ObservableMealComponentAndFood, onDelete: (component: MealViewModel.ObservableMealComponentAndFood) -> Unit) {
    DeleteButton {
        onDelete(component)
    }
}



