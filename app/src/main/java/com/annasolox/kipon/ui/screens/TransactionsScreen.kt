package com.annasolox.kipon.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.annasolox.kipon.ui.composables.AccountSearchBar
import com.annasolox.kipon.ui.composables.accounts.LazyAccountContributions
import com.annasolox.kipon.ui.viewmodels.UserViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.IntOffset
import com.annasolox.kipon.ui.composables.accounts.AccountContribution
import com.annasolox.kipon.ui.composables.accounts.UserContribuition

@Composable
fun TransactionsScreen(
    userViewModel: UserViewModel,
) {
    val allUserSavings by userViewModel.allUserSavings.observeAsState(emptyList())
    var query by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        userViewModel.getSavingsFromUser()
    }

    Box(Modifier
        .fillMaxSize()
        .padding(16.dp), contentAlignment = Alignment.TopCenter) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AccountSearchBar(
                    query = query,
                    onQueryChange = { query = it },
                    modifier = Modifier.weight(1f)
                )

                Spacer(Modifier.size(8.dp))

                IconButton(
                    {},
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.Black, shape = CircleShape)
                ) {
                    Icon(Icons.Default.FilterAlt, "Filter icon")
                }
            }

            val filteredAccounts = allUserSavings.filter {
                it.accountName.contains(query, ignoreCase = true)
            }

            allUserSavings.let {
                LazyColumn(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp)
                ) {
                    items(filteredAccounts, key = { it.id }) {
                        UserContribuition(it)
                        HorizontalDivider(Modifier.fillMaxWidth(), 1.dp, Color.Gray)
                    }
                }
            }
        }
    }
}