package com.wilde.caloriecounter2.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.wilde.caloriecounter2.composables.screens.Meal
import com.wilde.caloriecounter2.viewmodels.MealViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MealFragment : Fragment() {

    companion object {
        fun newInstance() = MealFragment()
    }

    private val mealViewModel: MealViewModel by activityViewModels()
    //private val foodListViewModel: FoodListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        /*return ComposeView(requireContext()).apply {
            setContent {

            }
        }*/

        //return inflater.inflate(R.layout.meal_fragment, container, false)
        return ComposeView(requireContext()).apply {
            setContent {
                Meal(mealViewModel)
            }
        }
    }


}