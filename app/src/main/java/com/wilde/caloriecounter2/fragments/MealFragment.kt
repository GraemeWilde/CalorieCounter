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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.lifecycle.coroutineScope
import com.wilde.caloriecounter2.viewmodels.MealViewModel
import com.wilde.caloriecounter2.data.food.entities.Product
import com.wilde.caloriecounter2.data.meals.entities.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

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

class MealFragment : Fragment() {

    companion object {
        fun newInstance() = MealFragment()
    }

    private val mealViewModel: MealViewModel by activityViewModels()

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


    //fun enumDropDown(listValues: String, selectedIndex: Int, onSelectedChange: (index: Int) -> Unit) {


    //@Preview
    @Composable
    fun MealView(mealViewModel: MealViewModel = this.mealViewModel) {
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
            /*Surface(
            ) {*/
            //val scrollState by remember { Sc}
            Column(
                modifier = Modifier
                    .padding(8.dp)
            ) {
                val mealName: String by mealViewModel.name.observeAsState("")
                TextField(
                    label = { Text("Meal Name") },
                    value = mealName,
                    onValueChange = {
                        mealViewModel.name.value = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                )

                val mealComponentsAndFoods =
                    remember { mealViewModel.observableMealComponentsAndFoods }

                ComponentsList(mealComponentsAndFoods, mealViewModel::removeComponentAndFood)
            }
        }
    }

    @Composable
    fun ComponentsList(
        mealComponentsAndFoods: MutableList<MealViewModel.ObservableMealComponentAndFood>?,
        onRemoveComponent: (component: MealViewModel.ObservableMealComponentAndFood) -> Unit
    ) {
        //val lazyListState = rememberLazyListState()
        /*var itemSize by remember { mutableStateOf(0) }
        var lColumnSize by remember { mutableStateOf(0) }*/

        /*val scrollSize by remember {
            derivedStateOf { itemSize * (mealComponentsAndFoods?.size ?: 0) }
        }*/

        /*val fullSize by remember {
            derivedStateOf {
                lifecycle.coroutineScope.launch {
                    Log.d("scroll", "fullSize")
                }
                itemSize * (mealComponentsAndFoods?.size ?: 0) * 4
            }
        }

        val scrollHeight by remember {
            derivedStateOf {
                lifecycle.coroutineScope.launch {
                    Log.d("scroll", "scrollHeight")
                }
                if (fullSize > 0) lColumnSize.toFloat() / fullSize * lColumnSize else 0f
            }
        }

        val offScreenAmount by remember {
            derivedStateOf {
                lifecycle.coroutineScope.launch {
                    Log.d("scroll", "offScreenAmount")
                }
                fullSize - lColumnSize
            }
        }

        val scrollMax by remember {
            derivedStateOf {
                lifecycle.coroutineScope.launch {
                    Log.d("scroll", "scrollMax")
                }
                lColumnSize - scrollHeight
            }
        }

        val scrollToCurrentItem by remember {
            derivedStateOf {
                lifecycle.coroutineScope.launch {
                    Log.d("scroll", "scrollToCurrentItem")
                }
                (lazyListState.firstVisibleItemIndex * itemSize).toFloat()
            }
        }

        val scrollPos by remember {
            derivedStateOf {
                lifecycle.coroutineScope.launch {
                    Log.d("scroll", "scrollPos")
                }
                if (offScreenAmount > 0) (scrollToCurrentItem + lazyListState.firstVisibleItemScrollOffset) / offScreenAmount * scrollMax else 0f
            }
        }*/

        /*val scroll by remember {
            derivedStateOf {
                object {

                    private val size = itemSize * (mealComponentsAndFoods?.size ?: 0) * 4
                    private val maxScroll = size - lColumnSize

                    //(1 - (scrollState.maxValue / this.size.height)) * (this.size.height - scrollState.maxValue)
                    val height: Float = if (size > 0) lColumnSize.toFloat() / size * lColumnSize else 0f
                    val pos: Float = if (maxScroll > 0) (lazyListState.firstVisibleItemIndex * itemSize + lazyListState.firstVisibleItemScrollOffset).toFloat() / maxScroll * (lColumnSize - height) else 0f

                    init {
                        lifecycle.coroutineScope.launch {
                            Log.d("Scroll", "$itemSize - $lColumnSize")
                            Log.d("ScrollMax", "$maxScroll")
                            Log.d(
                                "ScrollPerc",
                                "${(lazyListState.firstVisibleItemIndex * itemSize + lazyListState.firstVisibleItemScrollOffset).toFloat() / size}"
                            )
                            Log.d("ScrollPos", "$pos - $height")
                        }
                    }
                }
            }
        }*/

        /*with(LocalDensity.current) {
            Log.d(
                "listState",
                "${lazyListState.firstVisibleItemIndex} - ${lazyListState.firstVisibleItemScrollOffset}"
            )
            Log.d("itemSize", "${itemSize}")
            Log.d("lSize", "${lColumnSize}")
        }*/


        val scrollState = rememberScrollState()

        Column(
            Modifier
                .verticalScroll(scrollState)
                .scrollbarVertical(scrollState = scrollState)
                /*.scrollbarVertical(
                    fullSize = itemSize * (mealComponentsAndFoods?.size ?: 0) * 4,
                    visibleSize = lColumnSize,
                    itemSize = itemSize,
                    itemCount = mealComponentsAndFoods?.size ?: 0,
                    currentItem = lazyListState.firstVisibleItemIndex,
                    currentItemOffset = lazyListState.firstVisibleItemScrollOffset,
                    isScrollInProgress = lazyListState.isScrollInProgress
                )*/
                /*.scrollbarVertical(
                    scrollbarOffset = scrollPos,
                    scrollbarHeight = scrollHeight,
                    isScrollInProgress = lazyListState.isScrollInProgress,
                    //lazyListState = lazyListState
                )*/
            ,
            //state = lazyListState,
            //.verticalScroll(scrollState)
            //.scrollbarVertical(scrollState = scrollState)
        ) {
            for (i in 1..4) {
                mealComponentsAndFoods?.forEach { component ->
                    Box {
                        Card(
                            border = BorderStroke(1.dp, Color(0x66000000)),
                            elevation = 4.dp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                            /*.border(
                                            1.dp,
                                            Color(0x66000000),
                                            shape = RoundedCornerShape(2.dp)
                                        )*/
                            //.padding(8.dp)
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
                                                //.padding(vertical = 4.dp)
                                                .fillMaxHeight()
                                                .aspectRatio(1f)
                                        )
                                    }
                                    /*Icon(
                                                    Icons.Filled.Delete,
                                                    null,
                                                    Modifier
                                                        .padding(vertical = 4.dp)
                                                        .fillMaxHeight()
                                                        .aspectRatio(1f)
                                                )*/
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
        }
    }

    fun Modifier.scrollbarVertical(
        fullSize: Int,
        visibleSize: Int,
        itemSize: Int,
        itemCount: Int,
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

        /*private val size = itemSize * (mealComponentsAndFoods?.size ?: 0) * 4
        private val maxScroll = size - lColumnSize*/

        //(1 - (scrollState.maxValue / this.size.height)) * (this.size.height - scrollState.maxValue)
        /*val height: Float = if (size > 0) lColumnSize.toFloat() / size * lColumnSize else 0f
        val pos: Float = if (maxScroll > 0) (lazyListState.firstVisibleItemIndex * itemSize + lazyListState.firstVisibleItemScrollOffset).toFloat() / maxScroll * (lColumnSize - height) else 0f*/

        val maxScroll = fullSize - visibleSize
        val scrollbarHeight = if (fullSize > 0) visibleSize.toFloat() / fullSize * visibleSize else 0f
        val scrollbarOffset = if (maxScroll > 0) (currentItem * itemSize + currentItemOffset).toFloat() / maxScroll * (visibleSize - scrollbarHeight) else 0f


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


    @Composable
    fun Modifier.scrollbarVertical(
        scrollbarOffset: Float,
        scrollbarHeight: Float,
        isScrollInProgress: Boolean,
        //lazyListState: LazyListState,
        color: Color = Color.Gray,
        width: Dp = 6.dp,
        maxAlpha: Float = 0.5f,
        durationFadeOut: Int = 1500,
        durationFadeIn: Int = 150,
        delayFadeOut: Int = 1000
    ): Modifier {

        val targetAlpha = if (isScrollInProgress) maxAlpha else 0f
        val duration = if (isScrollInProgress) durationFadeIn else durationFadeOut

        val alpha by animateFloatAsState(
            targetValue = targetAlpha,
            animationSpec = tween(
                duration,
                delayMillis = if (isScrollInProgress) 0 else delayFadeOut
            ),
        )

        return drawWithContent {
            drawContent()

            //val size = this.size
            //Log.d("Size", "${size.height.toDp()}")


            /*val scrollbarHeight =
                (1 - (scrollState.maxValue / this.size.height)) * (this.size.height - scrollState.maxValue)*/
            /*val scrollbarOffset =
                (this.size.height - scrollbarHeight) * (scrollState.value.toFloat() /
                        scrollState.maxValue)*/

            if (scrollbarHeight > 0) {
                drawRoundRect(
                    color = color,
                    topLeft = Offset(this.size.width - width.toPx(), scrollbarOffset),
                    size = Size(width.toPx(), scrollbarHeight),
                    alpha = alpha,
                    cornerRadius = CornerRadius(10f, 10f),
                    //style =
                )
            }
        }
    }

    @Composable
    fun Modifier.scrollbarVertical(
        scrollState: ScrollState,
        color: Color = Color.Gray,
        width: Dp = 6.dp,
        maxAlpha: Float = 0.5f,
        durationFadeOut: Int = 1500,
        durationFadeIn: Int = 150,
        delayFadeOut: Int = 1000
    ): Modifier {

        val targetAlpha = if (scrollState.isScrollInProgress) maxAlpha else 0f
        val duration = if (scrollState.isScrollInProgress) durationFadeIn else durationFadeOut

        val alpha by animateFloatAsState(
            targetValue = targetAlpha,
            animationSpec = tween(
                duration,
                delayMillis = if (scrollState.isScrollInProgress) 0 else delayFadeOut
            ),
        )

        return drawWithContent {
            drawContent()

            val size = this.size


            val scrollbarHeight =
                (1 - (scrollState.maxValue / this.size.height)) * (this.size.height - scrollState.maxValue)
            val scrollbarOffset =
                (this.size.height - scrollbarHeight) * (scrollState.value.toFloat() /
                        scrollState.maxValue)

            /*drawRect(
                color = Color.Blue,
                topLeft = Offset(this.size.width - width.toPx(), scrollbarOffset),
                size = Size(width.toPx(), scrollbarHeight),
                alpha = 0.5f,
            )*/
            drawRoundRect(
                color = color,
                topLeft = Offset(this.size.width - width.toPx(), scrollbarOffset),
                size = Size(width.toPx(), scrollbarHeight),
                alpha = alpha,
                cornerRadius = CornerRadius(10f, 10f),
                //style =
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
        val icon = if (expanded)
            Icons.Filled.ArrowDropUp
        else
            Icons.Filled.ArrowDropDown

        Box(modifier = modifier) {
            TextField(
                value = selectedEnum.name,
                label = label,
                onValueChange = { },
                trailingIcon = {
                    Icon(imageVector = icon, contentDescription = null)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        expanded = true
                    }
                    .onSizeChanged {
                        dropDownWidth = it.width
                    }
                    /*.onGloballyPositioned {
                        dropDownPos = it.positionInWindow()
                    }*/
                ,
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