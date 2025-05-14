package com.annasolox.kipon.ui.composables.textFields

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerTextField(
    date: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    label: String = "Date goal",
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var showPicker by remember { mutableStateOf(false) }
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    var hasFocus by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = date?.atStartOfDay(ZoneId.systemDefault())?.toInstant()?.toEpochMilli(),
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis >= System.currentTimeMillis()
            }
        }
    )

    if(showPicker){
        DatePickerDialog(
            onDismissRequest = { showPicker = false },
            confirmButton = {
                Button(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            val selectedDate = Instant.ofEpochMilli(it)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                            onDateSelected(selectedDate)
                        }
                        showPicker = false
                    }
                ) {
                    Text("Ok")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showPicker = false }
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker( state = datePickerState)
        }
    }


    OutlinedTextField(
        value = date?.format(formatter) ?: "",
        onValueChange = { },
        readOnly = true,
        label = { label },
        shape = RoundedCornerShape(100f),
        modifier = modifier.onFocusChanged {
            if (it.isFocused && !hasFocus) {
                hasFocus = true
                showPicker = true
            } else if (!it.isFocused) {
                hasFocus = false
            }
        },
        trailingIcon =
        {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Date picker icon",
            )
        }
    )
}