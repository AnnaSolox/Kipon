package com.annasolox.kipon.ui.composables.textFields

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.annasolox.kipon.R

@Composable
fun RegisterPasswordTextField(text: String, label: String, onValueChanged: (String) -> Unit ){
    var passwordHidden by remember { mutableStateOf(true) }
    TextField(
        value = text,
        onValueChange = { onValueChanged(it) },
        singleLine = true,
        label = { Text(text = label) },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            errorContainerColor = Color.Transparent
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,
        trailingIcon = {
            IconButton({ passwordHidden = !passwordHidden }) {
                Icon(
                    painter = painterResource(if (passwordHidden) R.drawable.visibility_on else R.drawable.visibility_off),
                    contentDescription = "Visibility icon"
                )
            }
        }
    )
}