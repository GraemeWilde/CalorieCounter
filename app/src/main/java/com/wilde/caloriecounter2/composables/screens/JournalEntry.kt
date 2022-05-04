package com.wilde.caloriecounter2.composables.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.util.Log
import android.widget.CalendarView
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.wilde.caloriecounter2.composables.other.DatePickerView
import com.wilde.caloriecounter2.composables.other.RunOnce
import com.wilde.caloriecounter2.composables.other.TextField
import com.wilde.caloriecounter2.composables.other.TimePickerView
import com.wilde.caloriecounter2.viewmodels.JournalEntryViewModel
import java.time.*
import java.time.format.DateTimeFormatter

@Preview
@Composable
fun JournalEntry(
    viewModel: JournalEntryViewModel = viewModel()
) {
    Column {
        val date: LocalDateTime by viewModel.observableJournalEntry?.date!!.collectAsState()

        DatePickerView(date.toLocalDate()) {
            viewModel.observableJournalEntry!!.date.value = it.atTime(date.toLocalTime())
        }

        TimePickerView(date.toLocalTime()) {
            viewModel.observableJournalEntry!!.date.value = it.atDate(date.toLocalDate())
        }

    }
}