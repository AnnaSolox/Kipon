package com.annasolox.kipon.ui.composables.textFields

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun PhotoTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    error: String? = null,
    enabled: Boolean,
    onImageClick: () -> Unit
){
    Column(modifier){
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            readOnly = true,
            enabled = enabled,
            placeholder = { Text(text = "Selecciona una imagen") },
            isError = error != null,
            label = { Text(text = label) },
            shape = RoundedCornerShape(100f),
            trailingIcon = {
                IconButton(
                    onClick = onImageClick,
                    enabled = enabled
                ) {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = "Seleccionar imagen"
                    )
                }
            },
            keyboardOptions = KeyboardOptions.Default,
            visualTransformation = VisualTransformation.None,
            modifier = modifier
        )
        if (error != null) {
            FormErrorText(error)
        }
    }
}