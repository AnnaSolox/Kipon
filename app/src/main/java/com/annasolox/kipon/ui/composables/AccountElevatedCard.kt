package com.annasolox.kipon.ui.composables

import android.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun AccountElevatedCard() {
    ElevatedCard(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_lock_idle_low_battery),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(.2f),
                contentScale = ContentScale.Crop,
            )
            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(.8f)
                    .padding(16.dp)
            ) {
                Spacer(Modifier.size(10.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        text = "Título de la hucha"
                    )
                    Text(style = MaterialTheme.typography.bodyMedium, text = "20/05/2025")
                }

                Spacer(Modifier.size(18.dp))

                Box(Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .background(Color.Red)) {
                    Text("Aquí van las imágenes de los usuarios")
                }

                Spacer(Modifier.size(18.dp))

                AccountProgressBar(6000, 12000)
            }
        }
    }
}