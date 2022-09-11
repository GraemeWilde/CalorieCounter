package com.wilde.caloriecounter2.composables.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
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
            rightExtra = { mealListVM, meal ->
                DeleteButton {
                    mealListVM.removeMeals(meal.meal)     //removeMeals(meal.meal)
                }
            },
            onSelect = onSelect,

            // 56 (size of floating add button) 16 (button bottom padding)
            // 8 (lazylist content padding)
            // Todo: Read floating button default size from somewhere
            endSpacerHeight = (56 + 16 * 2 - 8 * 2).dp
        )

        SlideInFloatingActionButton(onAdd)
    }
}


@Composable
fun MealList(
    mealListViewModel: MealListViewModel,
    rightExtra: @Composable ((mealListViewModel: MealListViewModel, meal: MealAndComponentsAndFoods) -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(8.dp, 8.dp),
    endSpacerHeight: Dp = 0.dp,
    onSelect: ((MealAndComponentsAndFoods) -> Unit)?
) {
    val meals = mealListViewModel.mealsList.observeAsState()
    
    LazyColumn(
        Modifier.padding(0.dp),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        meals.value?.let {
            items(
                items = it,
                key = { meal ->
                    meal.meal.id
                }
            ) { meal ->
                MealCard(
                    meal = meal,
                    onSelect = if (onSelect != null) { { onSelect.invoke(meal) } } else null,
                    rightExtra = if (rightExtra != null) { { rightExtra(mealListViewModel, meal) } } else null
                )
            }
            // Add a bottom padding item (to allow overscroll past a floating button)
            if (endSpacerHeight > 0.dp) {
                item {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(endSpacerHeight),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("End of Meal List")
                    }
                }
            }
        }
    }
}

@Composable
fun MealCard(
    meal: MealAndComponentsAndFoods,
    modifier: Modifier = Modifier,
    onSelect: (() -> Unit)? = null,
    showLabel: Boolean = false,
    rightExtra: (@Composable () -> Unit)? = null
) {
    Card(
        border = BorderStroke(1.dp, MaterialTheme.colors.onBackground.copy(0.4f)),
        elevation = 0.dp,
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)

            // If onSelect is not null, add a clickable that calls it
            .then(
                other = onSelect?.let {
                    Modifier.clickable(onClick = it)
                } ?: Modifier
            )
        ,
    ) {
        Column(
            Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            if (showLabel) {
                Text(
                    "Meal",
                    modifier = Modifier.padding(start = 0.dp, bottom = 0.dp),
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.onSurface.copy(ContentAlpha.medium)
                )
            }
            Row(
                Modifier
                    .fillMaxWidth()
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
                                    8.dp.roundToPx()
                                } else {
                                    0
                                }
                            },
                            { column, _, _, _ ->
                                if (column % 2 == 1) {
                                    HorizontalAlignment.End
                                } else {
                                    HorizontalAlignment.Start
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
                // If right extra is not null, run it
                rightExtra?.invoke()
            }
        }
    }
}