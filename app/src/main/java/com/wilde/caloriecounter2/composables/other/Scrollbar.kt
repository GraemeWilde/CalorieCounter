package com.wilde.caloriecounter2.composables.other

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.scrollbarVertical(
    lazyListState: LazyListState,
    color: Color = Color.Gray,
    width: Dp = 6.dp,
    maxAlpha: Float = 0.5f,
    durationFadeOut: Int = 1500,
    durationFadeIn: Int = 150,
    delayFadeOut: Int = 1000
): Modifier = composed {


    val targetAlpha = if (lazyListState.isScrollInProgress) maxAlpha else 0f
    val duration = if (lazyListState.isScrollInProgress) durationFadeIn else durationFadeOut

    val alpha by animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec = tween(
            duration,
            delayMillis = if (lazyListState.isScrollInProgress) 0 else delayFadeOut
        ),
    )

    //lLS.layoutInfo.visibleItemsInfo.

    drawWithContent {
        drawContent()

        if ((lazyListState.isScrollInProgress || alpha > 0f) && lazyListState.layoutInfo.visibleItemsInfo.firstOrNull()?.index != null) {



            Log.d("scrollHeight", this.size.height.toString())
            Log.d("scrollVItems", lazyListState.layoutInfo.visibleItemsInfo.size.toString())
            Log.d("scrollTotalItems", lazyListState.layoutInfo.totalItemsCount.toString())
            Log.d("scrollLastIndex", "${lazyListState.layoutInfo.visibleItemsInfo.lastIndex}")
            Log.d("scrollOffsets", "${lazyListState.layoutInfo.viewportStartOffset} - ${lazyListState.layoutInfo.viewportEndOffset}")
            //lazyListState

            /*drawRoundRect(
                color = color,
                topLeft = Offset(this.size.width - width.toPx(), scrollbarOffset),
                size = Size(width.toPx(), scrollbarHeight),
                alpha = alpha,
                cornerRadius = CornerRadius(10f, 10f),
            )*/
        }
    }
}

fun Modifier.scrollbarVertical(
    fullSize: Int,
    visibleSize: Int,
    itemSize: Int,
    currentItem: Int,
    currentItemOffset: Int,
    isScrollInProgress: Boolean,
    color: Color = Color.Gray,
    width: Dp = 6.dp,
    maxAlpha: Float = 0.5f,
    durationFadeOut: Int = 1500,
    durationFadeIn: Int = 150,
    delayFadeOut: Int = 1000
): Modifier = composed {

    val maxScroll = fullSize - visibleSize
    val scrollbarHeight =
        if (fullSize > 0) visibleSize.toFloat() / fullSize * visibleSize else 0f
    val scrollbarOffset =
        if (maxScroll > 0) (currentItem * itemSize + currentItemOffset).toFloat() / maxScroll * (visibleSize - scrollbarHeight) else 0f


    val targetAlpha = if (isScrollInProgress) maxAlpha else 0f
    val duration = if (isScrollInProgress) durationFadeIn else durationFadeOut

    val alpha by animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec = tween(
            duration,
            delayMillis = if (isScrollInProgress) 0 else delayFadeOut
        ),
    )

    val lLS = rememberLazyListState()

    //lLS.layoutInfo.visibleItemsInfo.

    drawWithContent {
        drawContent()

        if (scrollbarHeight > 0) {
            drawRoundRect(
                color = color,
                topLeft = Offset(this.size.width - width.toPx(), scrollbarOffset),
                size = Size(width.toPx(), scrollbarHeight),
                alpha = alpha,
                cornerRadius = CornerRadius(10f, 10f),
            )
        }
    }
}


fun Modifier.scrollbarVertical(
    scrollbarOffset: Float,
    scrollbarHeight: Float,
    isScrollInProgress: Boolean,
    color: Color = Color.Gray,
    width: Dp = 6.dp,
    maxAlpha: Float = 0.5f,
    durationFadeOut: Int = 1500,
    durationFadeIn: Int = 150,
    delayFadeOut: Int = 1000
): Modifier = composed {

    val targetAlpha = if (isScrollInProgress) maxAlpha else 0f
    val duration = if (isScrollInProgress) durationFadeIn else durationFadeOut

    val alpha by animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec = tween(
            duration,
            delayMillis = if (isScrollInProgress) 0 else delayFadeOut
        ),
    )

    drawWithContent {
        drawContent()

        if (scrollbarHeight > 0) {
            drawRoundRect(
                color = color,
                topLeft = Offset(this.size.width - width.toPx(), scrollbarOffset),
                size = Size(width.toPx(), scrollbarHeight),
                alpha = alpha,
                cornerRadius = CornerRadius(10f, 10f),
            )
        }
    }
}

fun Modifier.scrollbarVertical(
    scrollState: ScrollState,
    color: Color = Color.Gray,
    width: Dp = 6.dp,
    maxAlpha: Float = 0.5f,
    durationFadeOut: Int = 1500,
    durationFadeIn: Int = 150,
    delayFadeOut: Int = 1000
): Modifier = composed {

    val targetAlpha = if (scrollState.isScrollInProgress) maxAlpha else 0f
    val duration = if (scrollState.isScrollInProgress) durationFadeIn else durationFadeOut

    val alpha by animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec = tween(
            duration,
            delayMillis = if (scrollState.isScrollInProgress) 0 else delayFadeOut
        ),
    )

    drawWithContent {
        drawContent()

        val size = this.size

        val scrollbarHeight =
            (1 - (scrollState.maxValue / this.size.height)) * (this.size.height - scrollState.maxValue)
        val scrollbarOffset =
            (this.size.height - scrollbarHeight) * (scrollState.value.toFloat() /
                    scrollState.maxValue)

        drawRoundRect(
            color = color,
            topLeft = Offset(this.size.width - width.toPx(), scrollbarOffset),
            size = Size(width.toPx(), scrollbarHeight),
            alpha = alpha,
            cornerRadius = CornerRadius(10f, 10f),
        )
    }
}