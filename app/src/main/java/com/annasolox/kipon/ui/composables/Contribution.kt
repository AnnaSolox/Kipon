package com.annasolox.kipon.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(showBackground = true)
@Composable
fun Contribution() {
    Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically) {
        UserThumbnail(Modifier.size(60.dp))

        Spacer(Modifier.size(20.dp))

        Column {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(style = MaterialTheme.typography.bodyMedium, text = "Clara Moreno García")
                Text(style = MaterialTheme.typography.titleSmall, text = "+500€")
            }

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(style = MaterialTheme.typography.bodySmall, text = "24/04/2025")
                Text(style = MaterialTheme.typography.bodySmall, text = "6000€")
            }
        }
    }
}
