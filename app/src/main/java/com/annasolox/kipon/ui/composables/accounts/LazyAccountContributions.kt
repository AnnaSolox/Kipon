package com.annasolox.kipon.ui.composables.accounts

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

@Composable
fun LazyAccountContributions(currentBoxSize: Dp, imageResource: Int){
    LazyColumn(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
            .offset {
                IntOffset(0, currentBoxSize.roundToPx())
            }
    ) {
        items(20, key = { it }) {
            Contribution(imageResource)
            HorizontalDivider(Modifier.fillMaxWidth(), 1.dp, Color.Gray)
        }
    }
}