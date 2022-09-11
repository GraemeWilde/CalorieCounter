package com.wilde.caloriecounter2.composables.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.wilde.caloriecounter2.composables.other.FlowRow
import com.wilde.caloriecounter2.data.food.entities.Product
import com.wilde.caloriecounter2.viewmodels.FoodListViewModel
import com.wilde.caloriecounter2.viewmodels.FoodListViewModelClass


data class FoodListColors(
    val foodBorderColor: Color = Color(0xFFCCCCCC),
    val textColor: Color = Color(0xFFEEEEEE),
    val productNameColor: Color = Color(0xFF333333),
    val productBrandColor: Color = Color(0xFF663333),
    val productQuantityColor: Color = Color(0xFF333366)
)


@Composable
fun FoodList(
    viewModel: FoodListViewModelClass = hiltViewModel<FoodListViewModel>(),
    foodListColors: FoodListColors = FoodListColors(),
    onSelect: ((selectedFood: Product) -> Unit)? = null
) {
    //val viewModel = viewModel(FoodListViewModel::class.java)

    val foods = viewModel.foods.observeAsState()

    FoodListContent(foods.value, foodListColors, onSelect)
}

@Composable
fun FoodListContent(
    foods: List<Product>?,
    foodListColors: FoodListColors = FoodListColors(),
    onSelect: ((selectedFood: Product) -> Unit)? = null
) {
    LazyColumn(
        Modifier
            .fillMaxWidth()
//            .padding(8.dp, 8.dp)
        ,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(top = 8.dp, bottom = 20.dp, start = 8.dp, end = 8.dp)
    ) {
        if (foods != null) items(foods) { food ->
            FoodCard(
                food = food,
                onSelect = onSelect?.let { { it(food) } },
                foodListColors = foodListColors,
                elevation = 8.dp
            )
        }
    }
}

@Composable
fun FoodCard(
    food: Product,
    modifier: Modifier = Modifier,
    onSelect: (() -> Unit)? = null,
    showLabel: Boolean = false,
    elevation: Dp = 0.dp,
    foodListColors: FoodListColors = FoodListColors()
) {
//    Surface(
//        modifier
//            //.padding(0.dp, 2.dp)
//            .then(onSelect?.let {
//                Modifier.clickable {
//                    it()
//                }
//            } ?: Modifier),
//        shape = RoundedCornerShape(15.dp),
//        border = BorderStroke(2.dp, foodListColors.foodBorderColor)
//    ) {

//    Card(
//        modifier
//            //.padding(0.dp, 2.dp)
//            .then(onSelect?.let {
//                Modifier.clickable {
//                    it()
//                }
//            } ?: Modifier),
//        border = BorderStroke(1.dp, MaterialTheme.colors.onBackground.copy(0.4f)),
//        elevation = 8.dp,
//        shape = MaterialTheme.shapes.medium
//    ) {

    Box(
        modifier
            //.padding(0.dp, 2.dp)
            .then(onSelect?.let {
                Modifier.clickable {
                    it()
                }
            } ?: Modifier)
            .shadow(elevation, MaterialTheme.shapes.medium)
            .background(MaterialTheme.colors.background, MaterialTheme.shapes.medium)
            .border(BorderStroke(1.dp, MaterialTheme.colors.onBackground.copy(0.4f)), MaterialTheme.shapes.medium)
        ,
        //border = BorderStroke(1.dp, MaterialTheme.colors.onBackground.copy(0.4f)),
        //elevation = 8.dp,
        //shape = MaterialTheme.shapes.medium
    ) {
        Column(
            Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            if (showLabel) {
                Text(
                    "Food",
                    modifier = Modifier.padding(start = 0.dp, bottom = 2.dp),
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.onSurface.copy(ContentAlpha.medium)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
//                Box(
//                    Modifier
//                    //.padding(start = 5.dp)
//                    //.size(12.dp, 20.dp)
//                    //.align(Alignment.CenterVertically)
//                ) {
//                    Icon(
//                        Icons.Filled.ChevronRight, null,
//                        Modifier.requiredSize(24.dp)
//                    )
//                }
                FlowRow(
                    Modifier
                        .fillMaxWidth(),
                    paddingBetweenHorizontal = 4.dp,
                    paddingBetweenVertical = 2.dp
                    /*.then(
                    if (onSelect != null) Modifier.clickable {
                        onSelect(food)
                    } else
                        Modifier
                )*/
                    //.padding(0.dp, 1.dp)
                ) {
                    TextBubble(
                        food.productName,
                        foodListColors.productNameColor,
                        foodListColors.textColor
                    )
                    if (food.brands.isNotBlank()) {
                        TextBubble(
                            food.brands,
                            foodListColors.productBrandColor,
                            foodListColors.textColor
                        )
                    }
                    food.packageSize?.let {
                        TextBubble(
                            it,
                            foodListColors.productQuantityColor,
                            foodListColors.textColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TextBubble(text: String, color: Color, contentColor: Color) {
    Surface(
        //Modifier.padding(2.dp, 1.dp),
        color = color,
        contentColor = contentColor,
        //shape = RoundedCornerShape(21.dp / 2),
        shape = RoundedCornerShape(50),
        border = BorderStroke(1.dp, MaterialTheme.colors.onBackground.copy(0.4f)),
        //elevation = 8.dp
    ) {
        Text(text,
            Modifier
                .widthIn(28.dp)
                .padding(7.dp, 4.dp), textAlign = TextAlign.Center)
    }
}