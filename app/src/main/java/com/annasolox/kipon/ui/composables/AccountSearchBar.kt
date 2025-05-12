package com.annasolox.kipon.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.annasolox.kipon.ui.models.AccountOverview

@Composable
fun AccountSearchBar(
    accounts: List<AccountOverview>,
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
    if (expanded && query.isNotBlank()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .offset(y = with(LocalDensity.current) {
                    textFieldSize.height.toDp()
                })
                .background(Color.White, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            LazyColumn {
                items(accounts.size) { index ->
                    val item = accounts[index]
                    if (item.name.contains(query, ignoreCase = true)) {
                        Text(
                            text = item.name,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier
                                .padding(8.dp)
                        )
                        HorizontalDivider(
                            color = Color.LightGray,
                            thickness = 1.dp,
                            modifier = Modifier
                                .padding(top = 8.dp, bottom = 8.dp)
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}