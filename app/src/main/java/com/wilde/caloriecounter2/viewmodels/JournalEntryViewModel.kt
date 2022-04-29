package com.wilde.caloriecounter2.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.PrimaryKey
import com.wilde.caloriecounter2.data.journal.JournalRepository
import com.wilde.caloriecounter2.data.journal.entities.FullJournalEntry
import com.wilde.caloriecounter2.data.journal.entities.JournalEntry
import com.wilde.caloriecounter2.data.other.quantity.Quantity
import com.wilde.caloriecounter2.viewmodels.helper.ObservableQuantity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class JournalEntryViewModel @Inject internal constructor(
    private val journalRepository: JournalRepository
): ViewModel() {
    //val date: MutableStateFlow<LocalDateTime>()
    class ObservableJournalEntry(journalEntry: JournalEntry? = null) {

        constructor(): this(null)

        val date = MutableStateFlow<LocalDateTime>(journalEntry?.date ?: LocalDateTime.now())

        val quantity = if (journalEntry != null) ObservableQuantity(journalEntry.quantity) else ObservableQuantity()

        val foodId = MutableStateFlow<Int?>(journalEntry?.foodId)

        val mealId = MutableStateFlow<Int?>(journalEntry?.mealId)

        val id = MutableStateFlow<Int>(journalEntry?.id ?: 0)
    }

    var observableJournalEntry: ObservableJournalEntry? = null

    fun openJournalEntry(journalEntry: JournalEntry) {
        observableJournalEntry = ObservableJournalEntry(journalEntry)
    }

    fun openJournalEntry(fullJournalEntry: FullJournalEntry) {
        openJournalEntry(fullJournalEntry.journalEntry)
    }

    fun save() {
        observableJournalEntry?.let {
            if (it.foodId.value != null || it.mealId.value != null) {
                val newJournalEntry = if (it.foodId.value != null) {
                    JournalEntry(
                        it.date.value,
                        Quantity(it.quantity.measurement.value!!, it.quantity.type.value!!),
                        it.foodId.value,
                        null,
                        0
                    )
                } else {
                    JournalEntry(
                        it.date.value,
                        Quantity(it.quantity.measurement.value!!, it.quantity.type.value!!),
                        null,
                        it.mealId.value,
                        0
                    )
                }
                CoroutineScope(Dispatchers.IO).launch {
                    journalRepository.insertJournalEntry(newJournalEntry)
                }
            }
        }
    }

    fun clear() {
        observableJournalEntry = ObservableJournalEntry()
    }
}