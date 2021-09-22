package com.wilde.caloriecounter2.fragments

import android.os.Bundle
import android.text.Layout
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.fragment.app.activityViewModels
import com.google.android.material.composethemeadapter.createMdcTheme
import com.wilde.caloriecounter2.viewmodels.MealViewModel
import com.wilde.caloriecounter2.data.food.entities.Product
import com.wilde.caloriecounter2.data.meals.entities.*

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
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    //Row {
                        val mealName: String by mealViewModel.name.observeAsState("")
                        /*Text(
                            text = mealName,
                            style = MaterialTheme.typography.h5.copy(textAlign = TextAlign.Left),
                            modifier = Modifier.fillMaxWidth(),
                        )*/
                        TextField(
                            label = { Text("Meal Name") },
                            value = mealName,
                            onValueChange = {
                                mealViewModel.name.value = it
                            },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    //}
                    mealViewModel.observableMealComponentsAndFoods.forEach { component ->
                        /*Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                *//*.background(
                                    MaterialTheme.colors.secondary,
                                    AbsoluteRoundedCornerShape(8.dp)
                                )*//*
                                .padding(8.dp)
                        ) {*/
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                                    .border(1.dp, Color(0x66000000), shape = RoundedCornerShape(2.dp))
                                    .padding(8.dp)
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
                                    Icon(Icons.Filled.Delete, null, Modifier.padding(vertical = 4.dp).fillMaxHeight().aspectRatio(1f))
                                }


                                //val measurement by component.quantity.measurement.observeAsState(0)

                                /*var measurement by remember {
                                    mutableStateOf(
                                        component.quantity.measurement.value.toString()
                                    )
                                }*/
                                Row(
                                    Modifier.padding(top = 4.dp)
                                ) {
                                    val measurementString by component.quantity.measurementString.observeAsState(
                                        "0"
                                    )

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
                        //}
                    }
                }
            //}
        }
    }
    @Composable
    fun <T: Enum<T>> EnumDropDown(
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
        var dropDownPos by remember { mutableStateOf(Offset.Zero) }
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
                    .onGloballyPositioned {
                        dropDownPos = it.positionInWindow()
                        Log.d("Globally", dropDownPos.toString())
                    }
                ,
                enabled = false,
                colors = tfColors,
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.width(
                    with(LocalDensity.current) {
                        Log.d("Width", dropDownWidth.toDp().toString())
                        Log.d("Position", "${dropDownPos.x.toDp().toString()} - ${dropDownPos.y.toDp().toString()}")
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