package com.annasolox.kipon.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.annasolox.kipon.ui.composables.AccountSearchBar

@Composable
fun TransactionsScreen(){
    Box(){
        Row {
            AccountSearchBar(
                query = "",
                onQueryChange = {},
                modifier = Modifier
                    .padding(horizontal = 15.dp)
            )

            IconButton({}) {
                Icon(Icons.Default.FilterAlt, "Filter icon")
            }
        }
    }
}