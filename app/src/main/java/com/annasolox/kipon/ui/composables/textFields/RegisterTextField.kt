package com.annasolox.kipon.ui.composables.textFields

import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun RegisterTextField(text: String, label: String, error: String? = null, onValueChanged: (String) -> Unit ){
    TextField(
        value = text,
        onValueChange = { onValueChanged(it) },
        singleLine = true,
        label = { Text(text = label) },
        isError = error != null,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            errorContainerColor = Color.Transparent
        )
    )
    if (error != null) {
        FormErrorText(error)
    }
}