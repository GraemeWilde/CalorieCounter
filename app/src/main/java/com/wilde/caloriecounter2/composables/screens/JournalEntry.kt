package com.wilde.caloriecounter2.composables.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wilde.caloriecounter2.composables.other.*
import com.wilde.caloriecounter2.composables.other.TextField
import com.wilde.caloriecounter2.data.other.quantity.QuantityType
import com.wilde.caloriecounter2.other.Macros
import com.wilde.caloriecounter2.viewmodels.FoodListViewModel
import com.wilde.caloriecounter2.viewmodels.JournalEntryViewModel
import com.wilde.caloriecounter2.viewmodels.MealListViewModel
import kotlinx.coroutines.android.awaitFrame
import java.time.LocalDateTime

@Preview
@Composable
fun JournalEntry(
    viewModel: JournalEntryViewModel = viewModel()
) {
    var selectFoodMeal by rememberSaveable { mutableStateOf(false) }

    val foodListViewModel: FoodListViewModel = hiltViewModel()
    val mealListViewModel: MealListViewModel = hiltViewModel()
    RunOnce {
        // Load food list
        foodListViewModel.getFoodsList()
    }

    // Dialog to select a food or meal
    if (selectFoodMeal) {
        DialogFix(
            onDismissRequest = {
                selectFoodMeal = false
            }
        ) {
            Surface(shape = RoundedCornerShape(16.dp), modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)) {
                JournalPanel("Pick Food or Meal:") {
                    var foodTab by rememberSaveable { mutableStateOf(true) }

                    // Tabs for Food or Meal selection
                    TabRow(
                        selectedTabIndex = if (foodTab) 0 else 1,
                        Modifier.shadow(4.dp)
                    ) {
                        CustomTab(selected = foodTab, onClick = { foodTab = true }) {
                            Text("Foods")
                        }
                        CustomTab(selected = !foodTab, onClick = { foodTab = false }) {
                            Text("Meals")
                        }
                    }

                    // Show food tab if foodTab = true, otherwise show meal tab
                    if (foodTab) {
                        FoodList(foodListViewModel) {
                            viewModel.selectFood(it)
                            Log.d("JournalEntry", "Select Food: ${it.productName}")
                            selectFoodMeal = false
                        }
                    } else {
                        MealList(
                            mealListViewModel = mealListViewModel,
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
                        ) {
                            viewModel.observableJournalEntry.quantity.type.value = QuantityType.Servings
                            viewModel.selectMeal(it)
                            Log.d("JournalEntry", "Select Meal: ${it.meal.name}")
                            selectFoodMeal = false
                        }
                    }
                }
            }
        }
    }

    var firstLaunch by remember { mutableStateOf(true) }

    Column(
        Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val date: LocalDateTime by viewModel.observableJournalEntry.date.collectAsState()

        DatePickerView(
            date.toLocalDate(),
            modifier = Modifier.fillMaxWidth()
        ) {
            viewModel.observableJournalEntry.date.value = it.atTime(date.toLocalTime())
        }

        TimePickerView(
            date.toLocalTime(),
            modifier = Modifier.fillMaxWidth()
        ) {
            viewModel.observableJournalEntry.date.value = it.atDate(date.toLocalDate())
        }

        val food by viewModel.observableJournalEntry.food.collectAsState()
        val meal by viewModel.observableJournalEntry.meal.collectAsState()

        when {
            food != null -> {
                FoodCard(
                    food = food!!,
                    onSelect = { selectFoodMeal = true },
                    showLabel = true
                )
            }
            meal != null -> {
                MealCard(
                    meal = meal!!,
                    onSelect = { selectFoodMeal = true },
                    showLabel = true
                )
            }
            else -> {
                Button(
                    onClick = {
                        selectFoodMeal = true
                    }
                ) {
                    Text("Select Food")
                }
            }
        }
        if (food != null || meal != null) {
            val measurementString by viewModel.observableJournalEntry.quantity.measurementString.observeAsState(" ")
            val type by viewModel.observableJournalEntry.quantity.type.observeAsState(QuantityType.Servings)

            val focusRequester = remember { FocusRequester() }
            val new by remember { mutableStateOf(!firstLaunch) }

            QuantityField(
                stringValue = measurementString,
                quantityType = type,
                enabledQuantityType = meal == null,
                onQuantityValueChange = viewModel.observableJournalEntry.quantity::measurementOnChange,
                onQuantityTypeChange = { viewModel.observableJournalEntry.quantity.type.value = it },
                focusRequester = focusRequester,
            )
//            { stringValue, quantityTypeValue ->
//                viewModel.observableJournalEntry.quantity.measurementOnChange(
//                    stringValue
//                )
//                viewModel.observableJournalEntry.quantity.type.value =
//                    quantityTypeValue
//            }

            if (new) {
                LaunchedEffect(true) {
                    awaitFrame()
                    focusRequester.requestFocus()
                }
            }


            /*Row(verticalAlignment = Alignment.CenterVertically) {
                TextField(
                    label = { Text("Measurement") },
                    value = measurement,
                    onValueChange = {},
                    readOnly = true
                )
                if (type == QuantityType.Ratio) {
                    Text("Servings")
                } else {
                    Text("g/ml")
                }
            }*/
            val measurementValue by viewModel.observableJournalEntry.quantity.measurement.observeAsState()

            val macros: Macros? = food?.let { product ->
                product.nutriments?.perServing?.let {
                    Macros(it.calories ?: 0f, it.proteins ?: 0f, it.fat ?: 0f, it.carbohydrates ?: 0f, measurementValue!!, type, it.servingSize)
                }
            } ?: meal?.let { meal ->
                Macros(meal.calorieSum, meal.proteinsSum, meal.fatSum, meal.carbohydratesSum, measurementValue!!, type)
            }

            with(LocalDensity.current) {
                PairsGrid(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 0.dp)
                        .weight(1f),
                    paddingBetween = { col, _ ->
                        if (col % 2 == 1) {
                            8.dp.roundToPx()
                        } else {
                            0
                        }
                    },
                    horizontalAlignment = { column, _, _, _ ->
                        if (column % 2 == 1) {
                            HorizontalAlignment.End
                        } else {
                            HorizontalAlignment.Start
                        }
                    }
                ) {
                    Text("Calories:")
                    Text(
                        macros?.calorieSum?.toString() ?: "N/A",
                        Modifier.width(72.dp),
                        textAlign = TextAlign.End
                    )
                    Text("Proteins:")
                    Text(
                        macros?.proteinsSum?.toString() ?: "N/A",
                        Modifier.width(72.dp),
                        textAlign = TextAlign.End
                    )
                    Text("Fat:")
                    Text(
                        macros?.fatSum?.toString() ?: "N/A",
                        Modifier.width(72.dp),
                        textAlign = TextAlign.End
                    )
                    Text("Carbs:")
                    Text(
                        macros?.carbohydratesSum?.toString() ?: "N/A",
                        Modifier.width(72.dp),
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
    RunOnceSaveable {
        firstLaunch = false
    }
}

/**
 * CustomTab to give tabs a consistent look
 */
@Composable
private fun CustomTab(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Tab(
        selected = selected, onClick = onClick, modifier = Modifier
            .padding(10.dp)
            .then(modifier), content = content
    )
}

@Composable
private fun JournalPanel(
    title: String,
    onTitleClick: () -> Unit = {},
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(4.dp)
            .shadow(4.dp, RoundedCornerShape(16.dp), clip = false)
            .background(MaterialTheme.colors.background, RoundedCornerShape(16.dp))
            .border(
                1.dp,
                MaterialTheme.colors.onBackground,
                RoundedCornerShape(16.dp)
            )
            .padding(0.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        Text(
            title,
            Modifier
                .clickable(onClick = onTitleClick)
                .fillMaxWidth()
                .padding(bottom = 0.dp)
                .zIndex(1f)
                .shadow(4.dp, RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp))
                .background(
                    MaterialTheme.colors.background,
                    RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp)
                )
                .border(
                    1.dp,
                    MaterialTheme.colors.onBackground,
                    RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp)
                )
                .padding(8.dp)
        )
        content()
    }
}

@Composable
private fun QuantityField(
    measurementString: String,
    onMeasurementChange: (String) -> Unit,

    quantityType: QuantityType,
    onQuantityTypeChange: (QuantityType) -> Unit,

    focusRequester: FocusRequester? = null,

    onCancel: () -> Unit,
    onSave: () -> Unit,
) {
    Column {
        Row(
            Modifier.padding(top = 4.dp)
        ) {
            val foc = LocalFocusManager.current
            //val focusRequester = remember { FocusRequester() }
            // Measurement Text Field
            TextField(
                label = { Text("Measurement") },
                value = measurementString,
                onValueChange = onMeasurementChange,
                //component.quantity::measurementOnChange,
                modifier = Modifier
                    .padding(end = 4.dp)
                    .weight(1f)
                    .then((if (focusRequester != null) Modifier.focusRequester(focusRequester) else Modifier))
                ,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = {
                    foc.moveFocus(FocusDirection.Next)
                })
            )

            EnumDropDownField(
                label = { Text("Measure Type") },
                //clazz = QuantityType::class.java,
                selectedEnum = quantityType,
                onSelectedChange = {
                    onQuantityTypeChange(it)
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 4.dp)
            )
        }
        Row {
            Button(onCancel) {
                Text("Cancel")
            }
            Button(onSave) {
                Text("Ok")
            }
        }
    }
}

@Composable
private fun QuantityField2(
    measurement: String,
    type: QuantityType,

    measurementString: String,
    onMeasurementChange: (String) -> Unit,

    quantityType: QuantityType,
    onQuantityTypeChange: (QuantityType) -> Unit,

    focusRequester: FocusRequester? = null,

    onCancel: ((dialogOpen: Boolean)->Unit) -> Unit,
    onSave: ((dialogOpen: Boolean)->Unit) -> Unit,
) {
    var dialogOpen by rememberSaveable { mutableStateOf(false) }

    if (dialogOpen) {
        DialogFix(
            onDismissRequest = {
                dialogOpen = false
            }
        ) {
            Surface(shape = RoundedCornerShape(16.dp), modifier = Modifier
                .fillMaxWidth()
            ) {
                JournalPanel("Enter Quantity:") {
                    QuantityField(
                        measurementString = measurementString,
                        onMeasurementChange = onMeasurementChange,
                        quantityType = quantityType,
                        onQuantityTypeChange = onQuantityTypeChange,
                        focusRequester = focusRequester,
                        onCancel = {
                            Log.d("Quan", "OnCancel QF2")
                            onCancel {
                                Log.d("Quan", "OnCancel QF2.Inside")
                                dialogOpen = it
                            }
                       },
                        onSave = {
                            Log.d("Quan", "OnSave QF2")
                            onSave {
                                Log.d("Quan", "OnSave QF2.Inside")
                                dialogOpen = it
                            }
                        },
                    )
                }
            }
        }
    }

    /*Text(
        text = "$measurement ${
            if (type == QuantityType.Ratio)
                "Servings"
            else
                "g/ml"
        }",
        Modifier.defaultMinSize(TextFieldDefaults.MinWidth, TextFieldDefaults.MinHeight).clickable { dialogOpen = true }
    )*/
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { dialogOpen = true }
    ) {
        TextField(
            label = { Text("Measurement") },
            value = measurement,
            onValueChange = { onMeasurementChange(it) },
            //readOnly = true,
            modifier = Modifier.widthIn(10.dp, Dp.Unspecified)//.defaultMinSize(0.dp)
        )
        if (type == QuantityType.Servings) {
            Text("Servings")
        } else {
            Text("g/ml")
        }
    }
}