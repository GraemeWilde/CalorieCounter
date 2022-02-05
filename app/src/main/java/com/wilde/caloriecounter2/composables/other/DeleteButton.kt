package com.wilde.caloriecounter2.composables.other

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DeleteButton(onDelete: () -> Unit) {
    Surface(
        Modifier,
        shape = RoundedCornerShape(4.dp),
        color = MaterialTheme.colors.error, //0xFFf5a620
        border = BorderStroke(1.dp, MaterialTheme.colors.onBackground.copy(0.4f)),
        elevation = 8.dp
    ) {
        IconButton(
            onClick = onDelete,
            Modifier.then(
                Modifier
                    //.fillMaxHeight()
                    //.aspectRatio(1f)
                    .size(42.dp)
            )
        ) {
            Icon(
                Icons.Filled.Delete,
                "Delete",
                Modifier
                    .fillMaxHeight(0.8f)
                    .aspectRatio(1f),
                MaterialTheme.colors.onError
            )
        }
    }
}