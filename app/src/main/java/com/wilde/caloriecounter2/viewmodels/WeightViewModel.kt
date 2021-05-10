package com.wilde.caloriecounter2.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.wilde.caloriecounter2.data.weight.Weight
import com.wilde.caloriecounter2.data.weight.WeightRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class WeightViewModel @Inject internal constructor(
    private val weightRepository: WeightRepository,
    //private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val weights: LiveData<List<Weight>> = weightRepository.getAll().asLiveData()

    fun insert(date: LocalDateTime, weight: Float) {
        viewModelScope.launch {
            val test = weightRepository.addWeight(date, weight)
            //Log.d("INSERT WEIGHT",  "${weightRepository.addWeight(date, weight)}")
            Log.d("TEST", "TEST")
        }
    }
}