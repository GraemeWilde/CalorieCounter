package com.wilde.caloriecounter2.composables.other

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun DatePickerView(
    date: LocalDate,
    modifier: Modifier = Modifier,
    onAccept: (LocalDate) -> Unit
) {
    val source = remember { MutableInteractionSource() }

    val context = LocalContext.current

    val datePicker = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
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

    //Box {
        TextField(
            label = { Text("Date") },
            leadingIcon = { Icon(Icons.Filled.CalendarToday, null) },
            value = date.format(DateTimeFormatter.ofPattern("yyyy MMM. dd")),
            onValueChange = {},
            readOnly = true,
            interactionSource = source,
            maxLines = 1,
            modifier = modifier
        )
        if (source.collectIsPressedAsState().value) {
            datePicker.show()
        }
    //}
}

@Composable
fun TimePickerView(
    time: LocalTime,
    modifier: Modifier = Modifier,
    onAccept: (LocalTime) -> Unit
) {
    val source = remember { MutableInteractionSource() }

    val context = LocalContext.current

    val timePicker = TimePickerDialog(
        context,
        { _: TimePicker, hour: Int, minute: Int ->
            onAccept(
                LocalTime.of(hour, minute)
            )
        },
        time.hour,
        time.minute,
        true
    )

    //Box {
        TextField(
            label = { Text("Time") },
            leadingIcon = { Icon(Icons.Filled.Schedule, null) },
            value = time.format(DateTimeFormatter.ofPattern("h:mm a")),
            onValueChange = {},
            readOnly = true,
            interactionSource = source,
            maxLines = 1,
            modifier = modifier
        )
        if (source.collectIsPressedAsState().value) {
            timePicker.show()
        }
    //}
}