package com.annasolox.kipon.ui.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize

@Composable
fun AccountSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    var expanded by remember { mutableStateOf(false) }
    var textFieldSize by remember { mutableStateOf(IntSize.Zero) }

    OutlinedTextField(
        value = query,
        onValueChange = {
            onQueryChange(it)
            expanded = it.isNotEmpty()
        },
        label = { Text("Search") },
        modifier = Modifier
            .fillMaxWidth(),
        singleLine = true,
        shape = MaterialTheme.shapes.large,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent
        ),
        trailingIcon = {
            IconButton(
                onClick = {
                    if (expanded) {
                        onQueryChange("")
                        expanded = false
                    }
                }
            ) {
                Icon(
                    if (expanded) Icons.Outlined.Close else Icons.Outlined.Search,
                    contentDescription = "Trailing icon"
                )
            }
        }
    )
}