package com.annasolox.kipon.ui.composables.buttons

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun OptionsButton(
    imageVector: ImageVector,
    onClickAction: () -> Unit = {}
){
    IconButton(
        onClick = { onClickAction()},
        Modifier
            .clip(CircleShape)
            .border(1.dp, Color.White, CircleShape)
    ) {
        Icon(imageVector = imageVector, contentDescription = "Options", tint = Color.White)
    }
}