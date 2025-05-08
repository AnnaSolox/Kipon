package com.annasolox.kipon.ui.composables.images


import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun ImageThumbnail(modifier: Modifier, imageResource: Int) {
    Image(
        painter = painterResource(imageResource),
        contentDescription = "User image thumbnail",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .clip(CircleShape)
            .border( 2.dp, Color.White, CircleShape)
    )
}