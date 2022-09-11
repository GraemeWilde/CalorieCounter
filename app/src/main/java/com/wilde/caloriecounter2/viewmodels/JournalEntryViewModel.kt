package com.wilde.caloriecounter2.viewmodels

import androidx.lifecycle.ViewModel
import com.wilde.caloriecounter2.data.food.entities.Product
import com.wilde.caloriecounter2.data.journal.JournalRepository
import com.wilde.caloriecounter2.data.journal.entities.FullJournalEntry
import com.wilde.caloriecounter2.data.journal.entities.JournalEntry
import com.wilde.caloriecounter2.data.meals.entities.MealAndComponentsAndFoods
import com.wilde.caloriecounter2.data.other.quantity.Quantity
import com.wilde.caloriecounter2.viewmodels.helper.ObservableQuantity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class JournalEntryViewModel @Inject internal constructor(
    private val journalRepository: JournalRepository
): ViewModel() {
    //val date: MutableStateFlow<LocalDateTime>()
    class ObservableJournalEntry(fullJournalEntry: FullJournalEntry? = null) {

        class MissingJournalFoodAndMealException() : Exception()

        constructor(): this(null)


        val date = MutableStateFlow<LocalDateTime>(
            fullJournalEntry?.journalEntry?.date ?: LocalDateTime.now()
        )

        val quantity = if (fullJournalEntry?.journalEntry != null) {
            ObservableQuantity(fullJournalEntry.journalEntry.quantity)
        } else {
            ObservableQuantity()
        }

        val food: MutableStateFlow<Product?> = MutableStateFlow(fullJournalEntry?.food)

        val meal: MutableStateFlow<MealAndComponentsAndFoods?> = MutableStateFlow(fullJournalEntry?.mealAndComponentsAndFoods)

        val id = MutableStateFlow<Int>(fullJournalEntry?.journalEntry?.id ?: 0)

        fun toJournalEntry(): JournalEntry {
            return food.value?.let { food ->
                JournalEntry(
                    date.value,
                    Quantity(quantity.measurement.value!!, quantity.type.value!!),
                    food.id,
                    null,
                    id.value
                )
            } ?: meal.value?.let { meal ->
                JournalEntry(
                    date.value,
                    Quantity(quantity.measurement.value!!, quantity.type.value!!),
                    null,
                    meal.meal.id,
                    id.value
                )
            } ?: throw MissingJournalFoodAndMealException()
        }
    }

    var observableJournalEntry: ObservableJournalEntry = ObservableJournalEntry()

    fun setFood(food: Product) {
        observableJournalEntry.food.value = food
        observableJournalEntry.meal.value = null
    }

    fun setMeal(meal: MealAndComponentsAndFoods) {
        observableJournalEntry.meal.value = meal
        observableJournalEntry.food.value = null
    }

    fun openJournalEntry(fullJournalEntry: FullJournalEntry) {
        observableJournalEntry = ObservableJournalEntry(fullJournalEntry)
    }

    fun selectFood(product: Product) {
        this.observableJournalEntry.food.value = product
        this.observableJournalEntry.meal.value = null
    }

    fun selectMeal(meal: MealAndComponentsAndFoods) {
        this.observableJournalEntry.meal.value = meal
        this.observableJournalEntry.food.value = null
    }

    // TODO Deal with exception from toJournalEntry
    fun save() {
        val saveJournalEntry = observableJournalEntry.toJournalEntry()

        CoroutineScope(Dispatchers.IO).launch {

            if (saveJournalEntry.id != 0) {
                journalRepository.updateJournalEntry(saveJournalEntry)
            } else {
                journalRepository.insertJournalEntry(saveJournalEntry)
            }
        }
    }

    fun delete() {
        if (observableJournalEntry.id.value != 0) {
            val deleteJournalEntry = observableJournalEntry.toJournalEntry()

            CoroutineScope(Dispatchers.IO).launch {
                journalRepository.deleteJournalEntry(deleteJournalEntry)
            }
        }
    }

    fun clear() {
        observableJournalEntry = ObservableJournalEntry()
    }
}