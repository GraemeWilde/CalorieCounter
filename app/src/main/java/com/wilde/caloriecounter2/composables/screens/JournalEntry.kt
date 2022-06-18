package com.wilde.caloriecounter2.composables.screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wilde.caloriecounter2.composables.other.*
import com.wilde.caloriecounter2.composables.other.TextField
import com.wilde.caloriecounter2.data.food.entities.Product
import com.wilde.caloriecounter2.data.meals.entities.MealAndComponentsAndFoods
import com.wilde.caloriecounter2.data.other.quantity.QuantityType
import com.wilde.caloriecounter2.viewmodels.FoodListViewModel
import com.wilde.caloriecounter2.viewmodels.JournalEntryViewModel
import com.wilde.caloriecounter2.viewmodels.MealListViewModel
import com.wilde.caloriecounter2.viewmodels.helper.ObservableQuantity
import java.time.LocalDateTime

enum class FoodOrMeal {
    Food,
    Meal
}

class TempFoodMealHolder private constructor(
    val food: Product? = null,
    val meal: MealAndComponentsAndFoods? = null
) {
    val type: FoodOrMeal

    init {
        assert((food == null).xor(meal == null))
        type =
            if (food != null) FoodOrMeal.Food
            else FoodOrMeal.Meal
    }

    constructor(food: Product) : this(food, null)
    constructor(meal: MealAndComponentsAndFoods) : this(null, meal)
}

@Preview
@Composable
fun JournalEntry(
    viewModel: JournalEntryViewModel = viewModel()
) {
    var selectFoodMeal by rememberSaveable { mutableStateOf(false) }

    val foodListViewModel: FoodListViewModel = hiltViewModel()
    val mealListViewModel: MealListViewModel = hiltViewModel()
    RunOnce {
        foodListViewModel.getFoodsList()
    }

    // Food / Meal selection Dialog
    if (selectFoodMeal) {
        DialogFix(
            onDismissRequest = {
                selectFoodMeal = false
            },
            modifier = Modifier.fillMaxHeight(0.8f)
        ) {

            // Which food tab we are on
            var foodTab by rememberSaveable { mutableStateOf(true) }
            var step1 by rememberSaveable { mutableStateOf(true) }

            RunOnceSaveable {
                viewModel.tempQuantity = ObservableQuantity()
            }

            Surface(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                Column {
                    JournalPanel("Pick Food:", onTitleClick = { step1 = true }) {

                        // Make content animate into visibility when panel title is clicked, hide when other titel is clicked
                        AnimatedVisibility(visible = step1) {
                            Column {

                                // Select Food or Meal
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
                                if (foodTab) {
                                    FoodList(foodListViewModel) {
                                        //viewModel.selectFood(it)
                                        viewModel.tempFoodMealHolder = TempFoodMealHolder(it)
                                        Log.d("JournalEntry", "Select Food: ${it.productName}")
                                        step1 = false
                                        //selectFoodMeal = false
                                    }
                                } else {
                                    MealList(
                                        mealListViewModel = mealListViewModel,
                                        contentPaddingBottom = false,
                                    ) {
                                        //viewModel.selectMeal(it)
                                        viewModel.tempFoodMealHolder = TempFoodMealHolder(it)
                                        Log.d("JournalEntry", "Select Meal: ${it.meal.name}")
                                        step1 = false
                                        //selectFoodMeal = false
                                    }
                                }

                            }
                        }
                    }
                    JournalPanel("Enter Quantity:", onTitleClick = { step1 = false }) {

                        // Make content animate into visibility when panel title is clicked, hide when other titel is clicked
                        AnimatedVisibility(visible = !step1) {
                            val measurementString by viewModel.tempQuantity!!.measurementString.observeAsState(
                                " "
                            )
                            val quantityType by viewModel.tempQuantity!!.type.observeAsState(QuantityType.Ratio)

                            QuantityField(
                                measurementString = measurementString,
                                onMeasurementChange = viewModel.tempQuantity!!::measurementOnChange,
                                quantityType = quantityType,
                                onQuantityTypeChange = { viewModel.tempQuantity!!.type.value = it },
                                onCancel = { selectFoodMeal = false },
                                onSave = {
                                    val quant = viewModel.tempQuantity?.toQuantity()
                                    val foodMeal = viewModel.tempFoodMealHolder

                                    if (quant != null && foodMeal != null) {
                                        if (foodMeal.type == FoodOrMeal.Food) {
                                            viewModel.observableJournalEntry.food.value = foodMeal.food
                                        } else {
                                            viewModel.observableJournalEntry.meal.value = foodMeal.meal
                                        }

                                        viewModel.observableJournalEntry.quantity.fromQuantity(quant)

                                        // Close Dialog
                                        selectFoodMeal = false
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    Column {
        val date: LocalDateTime by viewModel.observableJournalEntry.date.collectAsState()

        DatePickerView(date.toLocalDate()) {
            viewModel.observableJournalEntry.date.value = it.atTime(date.toLocalTime())
        }

        TimePickerView(date.toLocalTime()) {
            viewModel.observableJournalEntry.date.value = it.atDate(date.toLocalDate())
        }

//        val foodId by viewModel.observableJournalEntry!!.foodId.collectAsState()
//        val mealId by viewModel.observableJournalEntry!!.mealId.collectAsState()

        val food by viewModel.observableJournalEntry.food.collectAsState()
        val meal by viewModel.observableJournalEntry.meal.collectAsState()

        when {
            food != null -> {
                TextField(
                    label = { Text("Food Name") },
                    value = food?.productName ?: "Error",
                    onValueChange = {},
                    readOnly = true
                )
            }
            meal != null -> {
                TextField(
                    label = { Text("Meal Name") },
                    value = meal?.meal?.name ?: "Error",
                    onValueChange = {},
                    readOnly = true
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
            val measurement by viewModel.observableJournalEntry.quantity.measurementString.observeAsState(" ")
            val type by viewModel.observableJournalEntry.quantity.type.observeAsState()
            Row(verticalAlignment = Alignment.CenterVertically) {
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
            }
        }
    }
}

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

            EnumDropDown(
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
            Button(
                onSave
            ) {
                Text("Ok")
            }
        }
    }
}