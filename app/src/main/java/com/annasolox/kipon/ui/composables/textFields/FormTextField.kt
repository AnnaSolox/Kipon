package com.annasolox.kipon.ui.composables.textFields

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun FormTextField(
    value: String,
    label: String,
    error: String?,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        singleLine = true,
        isError = error != null,
        label = { Text(text = label) },
        shape = RoundedCornerShape(100f),
        visualTransformation = VisualTransformation.None
    )
    if (error != null){
        FormErrorText(error)
    }
}