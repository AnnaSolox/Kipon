package com.annasolox.kipon.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.annasolox.kipon.R
import com.annasolox.kipon.ui.composables.AccountSearchBar
import com.annasolox.kipon.ui.composables.accounts.UserContribuition
import com.annasolox.kipon.ui.composables.textFields.FormTextField
import com.annasolox.kipon.ui.viewmodels.UserViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    userViewModel: UserViewModel,
) {
    val allUserSavings by userViewModel.allUserSavings.observeAsState(emptyList())
    var query by remember { mutableStateOf("") }

    // Filter min and max values
    var minAmountText by remember { mutableStateOf("") }
    var maxAmountText by remember { mutableStateOf("") }

    var appliedMinAmount by remember { mutableStateOf<Float?>(null) }
    var appliedMaxAmount by remember { mutableStateOf<Float?>(null) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    var isSheetOpen by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        userViewModel.getSavingsFromUser()
    }

    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp), contentAlignment = Alignment.TopCenter
    ) {
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
                    onClick = {
                        if (appliedMinAmount != null || appliedMaxAmount != null) {
                            // Limpiar filtros sin abrir modal
                            appliedMinAmount = null
                            appliedMaxAmount = null
                        } else {
                            // Abrir el modal solo si no hay filtros aplicados
                            isSheetOpen = true
                            coroutineScope.launch { sheetState.show() }
                        }
                    },
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.Black, shape = CircleShape)
                ) {
                    if (appliedMinAmount != null || appliedMaxAmount != null) {
                        Icon(Icons.Default.Close, stringResource(R.string.cd_clear_filter))
                    } else {
                        Icon(Icons.Default.FilterAlt, stringResource(R.string.cd_filter_icon))
                    }
                }
            }

            if(allUserSavings.isEmpty()){
                Box(Modifier.fillMaxSize().padding(16.dp),
                    contentAlignment = Alignment.TopStart){
                    Text(stringResource(R.string.no_saving_text))
                }
            }

            val filteredAccounts = allUserSavings.filter {
                it.accountName.contains(query, ignoreCase = true) &&
                        (appliedMinAmount?.let { min -> it.amount >= min } != false) &&
                        (appliedMaxAmount?.let { max -> it.amount <= max } != false)
            }

            if(allUserSavings.isNotEmpty()){
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

    if (isSheetOpen) {

        ModalBottomSheet(
            onDismissRequest = { isSheetOpen = false },
            sheetState = sheetState,
            containerColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    stringResource(R.string.filter_amount_of_contribution_text).uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                )

                Row(Modifier.fillMaxWidth()) {
                    Box(Modifier.weight(1f)){
                        FormTextField(
                            value = minAmountText,
                            label = stringResource(R.string.min_amount_label),
                            error = null,
                            keyboardType = KeyboardType.Number,
                        ) {
                            minAmountText = it
                        }
                    }

                    Spacer(Modifier.size(8.dp))

                    Box(Modifier.weight(1f)){
                        FormTextField(
                            value = maxAmountText,
                            label = stringResource(R.string.max_amount_label),
                            error = null,
                            keyboardType = KeyboardType.Number,
                        ) {
                            maxAmountText = it
                        }
                    }
                }
                Spacer(Modifier.size(8.dp))
                Button(
                    onClick = {
                        appliedMinAmount = minAmountText.toFloatOrNull()
                        appliedMaxAmount = maxAmountText.toFloatOrNull()
                        isSheetOpen = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary
                    )
                ) {
                    Text(stringResource(R.string.apply_filter_text))
                }
            }
        }
    }
}