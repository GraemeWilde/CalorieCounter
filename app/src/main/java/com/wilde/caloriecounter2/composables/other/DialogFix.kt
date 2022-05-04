package com.wilde.caloriecounter2.composables.other

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties


fun Modifier.fillMaxWidth(lessWidth: Dp): Modifier {
    return fillMaxSize(lessWidth, null)
}

fun Modifier.fillMaxHeight(lessHeight: Dp): Modifier {
    return fillMaxSize(null, lessHeight)
}

fun Modifier.fillMaxSize(lessWidthAndHeight: Dp): Modifier {
    return fillMaxSize(lessWidthAndHeight, lessWidthAndHeight)
}

fun Modifier.fillMaxSize(lessWidth: Dp? = null, lessHeight: Dp? = null): Modifier {
    return this.layout { measurable: Measurable, constraints: Constraints ->

        val newConstraints = constraints.copy(
            maxWidth = lessWidth?.let { constraints.maxWidth - lessWidth.roundToPx() } ?: constraints.maxWidth,
            minWidth = lessWidth?.let { constraints.maxWidth - lessWidth.roundToPx() } ?: constraints.minWidth,
            maxHeight = lessHeight?.let { constraints.maxHeight - lessHeight.roundToPx() } ?: constraints.maxWidth,
            minHeight = lessHeight?.let { constraints.maxHeight - lessHeight.roundToPx() } ?: constraints.minWidth
        )

        val placeable = measurable.measure(newConstraints)

        layout(placeable.width, placeable.height) {
            placeable.place(0,0)
        }
    }
}

@Composable
// TODO Remove workaround once they fix it
fun DialogFix(
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = @OptIn(ExperimentalComposeUiApi::class) DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Box(Modifier.fillMaxWidth(64.dp)) {
            content()
        }
    }
}