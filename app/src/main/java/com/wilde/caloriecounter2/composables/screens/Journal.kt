package com.wilde.caloriecounter2.composables.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wilde.caloriecounter2.composables.other.FlowRow
import com.wilde.caloriecounter2.data.journal.entities.FullJournalEntry
import com.wilde.caloriecounter2.data.journal.entities.JournalEntry
import com.wilde.caloriecounter2.viewmodels.JournalViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

// For stickyHeader
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Journal(
    viewModel: JournalViewModel = viewModel(),
    onAdd: () -> Unit,
    onSelect: (() -> Unit)? = null //TODO
) {

    val entries = viewModel.entries.observeAsState()

    Box(Modifier.fillMaxSize()) {
//        Column {
//            entries.value?.forEach {
//                Text(it.journalEntry.date.toString())
//                if (it.mealAndComponentsAndFoods != null) {
//                    Text(it.mealAndComponentsAndFoods.meal.name)
//                } else if (it.food != null) {
//                    Text(it.food.productName)
//                }
//            }
//        }

        val grouped = produceState<Map<LocalDate, List<FullJournalEntry>>>(initialValue = emptyMap(), entries.value) {
            value = entries.value?.let { entries ->
                var tempGroup: MutableList<FullJournalEntry> = mutableListOf()
                var lastDate: LocalDate = entries.first().journalEntry.date.toLocalDate()
                val groupedEntries: MutableMap<LocalDate, List<FullJournalEntry>> = mutableMapOf()

                entries.forEach {
                    val date = it.journalEntry.date.toLocalDate()
                    if (date != lastDate) {
                        groupedEntries[lastDate] = tempGroup
                        lastDate = date
                        tempGroup = mutableListOf()
                    }

                    tempGroup.add(it)
                }
                groupedEntries[lastDate] = tempGroup

                groupedEntries
            } ?: emptyMap<LocalDate, List<FullJournalEntry>>()
        }

        LazyColumn(Modifier.fillMaxSize()) {
            grouped.value.forEach { groupedByDate ->
                stickyHeader {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .background(Color.Gray)) {
                        // groupedByDate.key is the date
                        Text(groupedByDate.key.toString())
                    }
                }
                // groupedByDate.value is the list of entries for a date
                items(groupedByDate.value) { entry ->
                    FlowRow(paddingBetween = 4.dp) {
                        Text(entry.journalEntry.date.toLocalTime().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)))
                        if (entry.mealAndComponentsAndFoods != null) {
                            Text(entry.mealAndComponentsAndFoods.meal.name)
                        } else if (entry.food != null) {
                            Text(entry.food.productName)
                        }
                    }
                }
            }
            item {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height((16 * 2 + 56).dp)
                        .background(Color.Gray.copy(0.6f)), contentAlignment = Alignment.Center) {
                    Text("End of Journal")
                }
            }
        }

        FloatingActionButton(
            onClick = onAdd,
            Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Filled.Add, null)
        }
    }
}