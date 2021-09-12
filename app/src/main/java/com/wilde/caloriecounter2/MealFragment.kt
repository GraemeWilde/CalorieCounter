package com.wilde.caloriecounter2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class MealFragment : Fragment() {

    companion object {
        fun newInstance() = MealFragment()
    }

    private lateinit var viewModel: MealViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /*return ComposeView(requireContext()).apply {
            setContent {

            }
        }*/

        return inflater.inflate(R.layout.meal_fragment, container, false)
    }

    /*//@Preview
    @Composable
    fun MealViewPreview() {
        *//*val meal = Meal(
            mealParent(
                name = "Test Meal"
            ),
            listOf(
                MealComponent(
                    MealComponentRef(
                        quantity = Quantity(
                            measurement = 2f,
                            type = QuantityType.Unit
                        )
                    ),
                    Product()
                )
            )
        )
        MealView(meal)*//*
    }

    //@Preview
    @Composable
    fun MealView(meal: Meal) {
        Column {
            Text(meal.mealParent.name)
        }
    }*/
}