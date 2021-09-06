package com.wilde.caloriecounter2.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.wilde.caloriecounter2.viewmodels.MealViewModel
import com.wilde.caloriecounter2.databinding.MealFragmentBinding

class MealFragment : Fragment() {

    private val mealViewModel: MealViewModel by viewModels()

    private var _binding: MealFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = MealFragmentBinding.inflate(inflater, container, false)



        return binding.root
    }
}