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
import com.wilde.caloriecounter2.composables.other.RunOnce
import com.wilde.caloriecounter2.composables.other.TextField
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

@Composable
fun DatePickerView(
    date: LocalDate,
    onDateSelected: ((LocalDate) -> Unit)? = null,
    onAccept: (LocalDate) -> Unit,
) {
    val source = remember { MutableInteractionSource() }

    val context = LocalContext.current

    val datePicker = DatePickerDialog(
        context,
        { datePicker: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            onAccept(
                LocalDate
                    .now()
                    .withMonth(month + 1)
                    .withYear(year)
                    .withDayOfMonth(dayOfMonth)
            )
        },
        date.year,
        date.monthValue - 1,
        date.dayOfMonth
    )

    Box {
        TextField(
            label = { Text("Date") },
            leadingIcon = { Icon(Icons.Filled.CalendarToday, null) },
            value = date.format(DateTimeFormatter.ofPattern("yyyy MMM. dd")),
            onValueChange = {},
            readOnly = true,
            interactionSource = source,
            maxLines = 1
        )
        if (source.collectIsPressedAsState().value) {
            datePicker.show()
        }
    }
}

@Composable
fun TimePickerView(
    time: LocalTime,
    onTimeSelected: ((LocalTime) -> Unit)? = null,
    onAccept: (LocalTime) -> Unit,
) {
    val source = remember { MutableInteractionSource() }

    val context = LocalContext.current

    val timePicker = TimePickerDialog(
        context,
        { timePicker: TimePicker, hour: Int, minute: Int ->
            onAccept(
                LocalTime.of(hour, minute)
            )
        },
        time.hour,
        time.minute,
        true
    )

    Box {
        TextField(
            label = { Text("Time") },
            leadingIcon = { Icon(Icons.Filled.Schedule, null) },
            value = time.format(DateTimeFormatter.ofPattern("h:mm a")),
            onValueChange = {},
            readOnly = true,
            interactionSource = source,
            maxLines = 1
        )
        if (source.collectIsPressedAsState().value) {
            timePicker.show()
        }
    }
}