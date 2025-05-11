package com.annasolox.kipon.ui.composables.textFields

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun LoginUsernameTextField(
    username: String,
    error: String?,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = username,
        onValueChange = { onValueChange(it) },
        singleLine = true,
        isError = error != null,
        label = { Text(text = "Username") },
        shape = RoundedCornerShape(100f)
    )
    if (error != null){
        FormErrorText(error)
    }
}