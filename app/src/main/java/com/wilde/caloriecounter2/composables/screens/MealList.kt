package com.wilde.caloriecounter2.composables.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wilde.caloriecounter2.composables.other.*
import com.wilde.caloriecounter2.data.meals.entities.MealAndComponentsAndFoods
import com.wilde.caloriecounter2.viewmodels.MealListViewModel

@Composable
fun MealListScreen(
    mealListViewModel: MealListViewModel = viewModel(),
    onAdd: () -> Unit,
    onSelect: ((MealAndComponentsAndFoods) -> Unit)?
) {
    Box(
        Modifier
            .fillMaxSize()
    ) {

        MealList(
            mealListViewModel = mealListViewModel,
            { mealListVM, meal ->
                DeleteButton {
                    mealListVM.removeMeals(meal.meal)     //removeMeals(meal.meal)
                }
            },
            onSelect = onSelect
        )

        SlideInFloatingActionButton(onAdd)
//        FloatingActionButton(
//            onClick = onAdd,
//            Modifier
//                .align(Alignment.BottomEnd)
//                .padding(16.dp)
//        ) {
//            Icon(Icons.Filled.Add, null)
//        }
    }
}


@Composable
fun MealList(
    mealListViewModel: MealListViewModel,
    rightExtra: @Composable ((mealListViewModel: MealListViewModel, meal: MealAndComponentsAndFoods) -> Unit)? = null,
    contentPaddingBottom: Boolean = true,
    onSelect: ((MealAndComponentsAndFoods) -> Unit)?
) {
    val meals = mealListViewModel.mealsList.observeAsState()

    LazyColumn(
        Modifier.padding(8.dp),
        // Add content padding to allow over scrolling so that the floating action button doesnt
        // cover the last entry, removable for journalEntry meal selection
        contentPadding = PaddingValues(bottom = if (contentPaddingBottom) 100.dp else 0.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        meals.value?.let {
            items(it) { meal ->
                Card(
                    border = BorderStroke(1.dp, MaterialTheme.colors.onBackground.copy(0.4f)),
                    elevation = 4.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onSelect?.invoke(meal)
                        }
                    ,
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .height(IntrinsicSize.Min),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(
                                meal.meal.name,
                                fontWeight = FontWeight.SemiBold
                            )
                            //FlowRow(Modifier.weight(1f), paddingBetween = 8.dp) {

                            with(LocalDensity.current) {
                                PairsGrid(
                                    Modifier
                                        .fillMaxWidth()
                                        .weight(1f),
                                    { col, _ ->
                                        if (col % 2 == 1) {
                                            16.dp.roundToPx()
                                        } else {
                                            0
                                        }
                                    },
                                    { column, _, _, _ ->
                                        if (column % 2 == 1) {
                                            VerticalAlignment.End
                                        } else {
                                            VerticalAlignment.Start
                                        }
                                    }
                                ) {
                                    Text("Calories:")
                                    Text(
                                        meal.calorieSum.toString(),
                                        Modifier.width(72.dp),
                                        textAlign = TextAlign.End
                                    )
                                    Text("Proteins:")
                                    Text(
                                        meal.proteinsSum.toString(),
                                        Modifier.width(72.dp),
                                        textAlign = TextAlign.End
                                    )
                                    Text("Fat:")
                                    Text(
                                        meal.fatSum.toString(),
                                        Modifier.width(72.dp),
                                        textAlign = TextAlign.End
                                    )
                                    Text("Carbs:")
                                    Text(
                                        meal.carbohydratesSum.toString(),
                                        Modifier.width(72.dp),
                                        textAlign = TextAlign.End
                                    )
                                }
                            }
                        }
                        rightExtra?.invoke(mealListViewModel, meal)
                    }
                }
            }
        }
    }
}