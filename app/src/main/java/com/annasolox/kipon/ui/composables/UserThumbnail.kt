package com.annasolox.kipon.ui.composables


import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.annasolox.kipon.R

@Composable
fun UserThumbnail(modifier: Modifier) {
    Image(
        painter = painterResource(R.drawable.girl_photo),
        contentDescription = "User image thumbnail",
        contentScale = ContentScale.Crop,
        modifier = modifier
            .clip(CircleShape)
            .border( 3.dp, Color.White, CircleShape)
    )
}