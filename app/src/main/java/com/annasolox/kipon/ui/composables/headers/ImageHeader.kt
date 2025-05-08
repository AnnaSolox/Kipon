package com.annasolox.kipon.ui.composables.headers

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp

@Composable
fun ImageHeader(
    height: Dp,
    imageResource: Int,
    contentImageDescription: String,
) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(height),
    ) {
        Image(
            painter = painterResource(imageResource),
            contentDescription = contentImageDescription,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .align(Alignment.Center)

        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.5f))
                    )
                )
        )
    }
}