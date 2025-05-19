package com.annasolox.kipon.ui.composables.textFields

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun FormTextField(
    value: String,
    label: String,
    error: String?,
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = { onValueChange(it) },
        singleLine = true,
        enabled = enabled,
        isError = error != null,
        label = { Text(text = label) },
        shape = RoundedCornerShape(100f),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        visualTransformation = VisualTransformation.None
    )
    if (error != null) {
        FormErrorText(error)
    }
}