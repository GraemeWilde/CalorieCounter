package com.wilde.caloriecounter2.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.wilde.caloriecounter2.R
import com.wilde.caloriecounter2.data.meals.MealListAdapter
import com.wilde.caloriecounter2.viewmodels.MealListViewModel
import com.wilde.caloriecounter2.databinding.MealListFragmentBinding
import com.wilde.caloriecounter2.viewmodels.MealViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MealListFragment : Fragment() {

    private val mealListViewModel: MealListViewModel by viewModels()
    private val mealViewModel: MealViewModel by activityViewModels()

    private var _binding: MealListFragmentBinding? = null
    private val binding get() = _binding!!

    private val adapter = MealListAdapter { meal ->
        //val bundle = bundleOf("meal" to meal)
        mealViewModel.openMeal(meal)
        findNavController().navigate(R.id.action_mealListFragment_to_mealFragment)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = MealListFragmentBinding.inflate(inflater, container, false)

        binding.mealsRecyclerView.adapter = adapter
        binding.mealsRecyclerView.layoutManager = LinearLayoutManager(this.context)

        mealListViewModel.mealsList.observe(viewLifecycleOwner) { meals ->
            adapter.submitList(meals)
        }

        return binding.root
    }
}