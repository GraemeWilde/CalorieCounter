package com.wilde.caloriecounter2.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.wilde.caloriecounter2.viewmodels.MealViewModel
import com.wilde.caloriecounter2.data.food.entities.Product
import com.wilde.caloriecounter2.data.meals.entities.*
import com.wilde.caloriecounter2.viewmodels.FoodListViewModel
import dagger.hilt.android.AndroidEntryPoint

private val primary = Color(0xFF9C0000)
private val primaryLight = Color(0xFFD5442B)
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

@AndroidEntryPoint
class MealFragment : Fragment() {

    companion object {
        fun newInstance() = MealFragment()
    }

    private val mealViewModel: MealViewModel by activityViewModels()
    private val foodListViewModel: FoodListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /*return ComposeView(requireContext()).apply {
            setContent {

            }
        }*/

        //return inflater.inflate(R.layout.meal_fragment, container, false)
        return ComposeView(requireContext()).apply {
            setContent {
                MealView()
            }
        }
    }

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
    fun MealView(mealViewModel: MealViewModel = this.mealViewModel, foodListViewModel: FoodListViewModel = this.foodListViewModel) {
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
                FoodList() {
                    mealViewModel.addComponentAndFood(it)
                    //MealViewModel.ObservableMealComponentAndFood()
                    selectingFood = false
                }
            else
                MealViewMealsList(mealViewModel) {
                    selectingFood = true
                }
        }
    }

    @Composable
    fun MealViewMealsList(viewModel: MealViewModel, onAddComponent: () -> Unit) {
        Box() {
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
        val lazyListState = rememberLazyListState()

        Column(
            Modifier
                .verticalScroll(scrollState)
                .scrollbarVertical(lazyListState)
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
                                        .fillMaxHeight()
                                        .aspectRatio(1f)
                                        .size(24.dp)
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

    fun Modifier.scrollbarVertical(
        lazyListState: LazyListState,
        color: Color = Color.Gray,
        width: Dp = 6.dp,
        maxAlpha: Float = 0.5f,
        durationFadeOut: Int = 1500,
        durationFadeIn: Int = 150,
        delayFadeOut: Int = 1000
    ): Modifier = composed {


        val targetAlpha = if (lazyListState.isScrollInProgress) maxAlpha else 0f
        val duration = if (lazyListState.isScrollInProgress) durationFadeIn else durationFadeOut

        val alpha by animateFloatAsState(
            targetValue = targetAlpha,
            animationSpec = tween(
                duration,
                delayMillis = if (lazyListState.isScrollInProgress) 0 else delayFadeOut
            ),
        )

        //lLS.layoutInfo.visibleItemsInfo.

        drawWithContent {
            drawContent()

            if ((lazyListState.isScrollInProgress || alpha > 0f) && lazyListState.layoutInfo.visibleItemsInfo.firstOrNull()?.index != null) {



                Log.d("scrollHeight", this.size.height.toString())
                Log.d("scrollVItems", lazyListState.layoutInfo.visibleItemsInfo.size.toString())
                Log.d("scrollTotalItems", lazyListState.layoutInfo.totalItemsCount.toString())
                Log.d("scrollLastIndex", "${lazyListState.layoutInfo.visibleItemsInfo.lastIndex}")
                Log.d("scrollOffsets", "${lazyListState.layoutInfo.viewportStartOffset} - ${lazyListState.layoutInfo.viewportEndOffset}")
                lazyListState

                /*drawRoundRect(
                    color = color,
                    topLeft = Offset(this.size.width - width.toPx(), scrollbarOffset),
                    size = Size(width.toPx(), scrollbarHeight),
                    alpha = alpha,
                    cornerRadius = CornerRadius(10f, 10f),
                )*/
            }
        }
    }

    fun Modifier.scrollbarVertical(
        fullSize: Int,
        visibleSize: Int,
        itemSize: Int,
        currentItem: Int,
        currentItemOffset: Int,
        isScrollInProgress: Boolean,
        color: Color = Color.Gray,
        width: Dp = 6.dp,
        maxAlpha: Float = 0.5f,
        durationFadeOut: Int = 1500,
        durationFadeIn: Int = 150,
        delayFadeOut: Int = 1000
    ): Modifier = composed {

        val maxScroll = fullSize - visibleSize
        val scrollbarHeight =
            if (fullSize > 0) visibleSize.toFloat() / fullSize * visibleSize else 0f
        val scrollbarOffset =
            if (maxScroll > 0) (currentItem * itemSize + currentItemOffset).toFloat() / maxScroll * (visibleSize - scrollbarHeight) else 0f


        val targetAlpha = if (isScrollInProgress) maxAlpha else 0f
        val duration = if (isScrollInProgress) durationFadeIn else durationFadeOut

        val alpha by animateFloatAsState(
            targetValue = targetAlpha,
            animationSpec = tween(
                duration,
                delayMillis = if (isScrollInProgress) 0 else delayFadeOut
            ),
        )

        val lLS = rememberLazyListState()

        //lLS.layoutInfo.visibleItemsInfo.

        drawWithContent {
            drawContent()

            if (scrollbarHeight > 0) {
                drawRoundRect(
                    color = color,
                    topLeft = Offset(this.size.width - width.toPx(), scrollbarOffset),
                    size = Size(width.toPx(), scrollbarHeight),
                    alpha = alpha,
                    cornerRadius = CornerRadius(10f, 10f),
                )
            }
        }
    }


    fun Modifier.scrollbarVertical(
        scrollbarOffset: Float,
        scrollbarHeight: Float,
        isScrollInProgress: Boolean,
        color: Color = Color.Gray,
        width: Dp = 6.dp,
        maxAlpha: Float = 0.5f,
        durationFadeOut: Int = 1500,
        durationFadeIn: Int = 150,
        delayFadeOut: Int = 1000
    ): Modifier = composed {

        val targetAlpha = if (isScrollInProgress) maxAlpha else 0f
        val duration = if (isScrollInProgress) durationFadeIn else durationFadeOut

        val alpha by animateFloatAsState(
            targetValue = targetAlpha,
            animationSpec = tween(
                duration,
                delayMillis = if (isScrollInProgress) 0 else delayFadeOut
            ),
        )

        drawWithContent {
            drawContent()

            if (scrollbarHeight > 0) {
                drawRoundRect(
                    color = color,
                    topLeft = Offset(this.size.width - width.toPx(), scrollbarOffset),
                    size = Size(width.toPx(), scrollbarHeight),
                    alpha = alpha,
                    cornerRadius = CornerRadius(10f, 10f),
                )
            }
        }
    }

    fun Modifier.scrollbarVertical(
        scrollState: ScrollState,
        color: Color = Color.Gray,
        width: Dp = 6.dp,
        maxAlpha: Float = 0.5f,
        durationFadeOut: Int = 1500,
        durationFadeIn: Int = 150,
        delayFadeOut: Int = 1000
    ): Modifier = composed {

        val targetAlpha = if (scrollState.isScrollInProgress) maxAlpha else 0f
        val duration = if (scrollState.isScrollInProgress) durationFadeIn else durationFadeOut

        val alpha by animateFloatAsState(
            targetValue = targetAlpha,
            animationSpec = tween(
                duration,
                delayMillis = if (scrollState.isScrollInProgress) 0 else delayFadeOut
            ),
        )

        drawWithContent {
            drawContent()

            val size = this.size

            val scrollbarHeight =
                (1 - (scrollState.maxValue / this.size.height)) * (this.size.height - scrollState.maxValue)
            val scrollbarOffset =
                (this.size.height - scrollbarHeight) * (scrollState.value.toFloat() /
                        scrollState.maxValue)

            drawRoundRect(
                color = color,
                topLeft = Offset(this.size.width - width.toPx(), scrollbarOffset),
                size = Size(width.toPx(), scrollbarHeight),
                alpha = alpha,
                cornerRadius = CornerRadius(10f, 10f),
            )
        }
    }

    @Composable
    fun <T : Enum<T>> EnumDropDown(
        clazz: Class<T>,
        selectedEnum: T,
        onSelectedChange: (select: T) -> Unit,
        label: @Composable (() -> Unit)? = null,
        modifier: Modifier = Modifier
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
}