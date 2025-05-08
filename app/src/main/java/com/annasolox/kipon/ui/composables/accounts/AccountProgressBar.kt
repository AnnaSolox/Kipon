package com.annasolox.kipon.ui.composables.accounts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AccountProgressBar(currentMoney: Int, moneyGoal: Int) {
    LinearProgressIndicator(
        modifier = Modifier
            .fillMaxWidth()
            .height(10.dp),
        progress = { .5f}
    )

    Spacer(Modifier.size(9.dp))

    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(style = MaterialTheme.typography.bodySmall, text = "$currentMoney €")
        Text(style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Black, text = "$moneyGoal €")
    }
}