package com.annasolox.kipon.ui.composables.textFields

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.annasolox.kipon.R

@Composable
fun LoginPasswordTextField(
    password: String,
    error: String?,
    onPasswordChange: (String) -> Unit,
) {
    var passwordHidden by remember { mutableStateOf(true) }
    OutlinedTextField(
        value = password,
        onValueChange = { onPasswordChange(it) },
        label = { Text(text = "Password") },
        singleLine = true,
        isError = error != null,
        shape = RoundedCornerShape(100f),
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
    if (error != null){
        FormErrorText(error)
    }
}