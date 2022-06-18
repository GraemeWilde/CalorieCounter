package com.wilde.caloriecounter2.composables.other

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import com.google.accompanist.insets.LocalWindowInsets
import kotlinx.coroutines.*
import kotlinx.coroutines.android.awaitFrame
import kotlin.random.Random

/**
 * TextField replacement that adds a fix to handle the issue with the software keyboard appearing
 * over the newly focused TextField. See [androidx.compose.material.TextField]
 */
@Composable
fun TextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape =
        MaterialTheme.shapes.small.copy(bottomEnd = ZeroCornerSize, bottomStart = ZeroCornerSize),
    colors: TextFieldColors = TextFieldDefaults.textFieldColors()
) {
    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()
    val job: MutableState<Job?> = remember { mutableStateOf(null) }
    @OptIn(ExperimentalFoundationApi::class)
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val ime = LocalWindowInsets.current.ime

    val id = remember { Random.nextInt(100000) }

    @OptIn(ExperimentalFoundationApi::class)
    androidx.compose.material.TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .focusRequester(focusRequester)
            .bringIntoViewRequester(bringIntoViewRequester)
            .onFocusEvent {
                if (it.isFocused && ime.isVisible) {
                    coroutineScope.launch { bringIntoViewRequester.bringIntoView() }
                    /*//Log.d("IME", "Focus Test")
                    if (job.value == null) {
                        Log.d("CustomTextField", "New Job + ${ if (job.value != null) "running" else "not running"} + ${coroutineScope.isActive}")
                        job.value = coroutineScope.launch {

                            Log.d("CustomTextField", "Launched + $id")
                            //Log.d("IME", "Focus Launch")
                            //delay(100)

                            *//*var count = 0
                            while (!ime.isVisible && count < 30 || ime.animationInProgress) {
                                count++
                                delay(100)
                            }*//*

                            var count = 0
                            while ((ime.animationInProgress || !ime.isVisible) && count < 20) {
                                delay(100)
                                yield()
                                awaitFrame()
                                //bringIntoViewRequester.bringIntoView()
                                count++
                            }

                            if (ime.isVisible) {
                                Log.d("CustomTextField", "IME visible $count")
                                bringIntoViewRequester.bringIntoView()
                                delay(100)
                                awaitFrame()
                                bringIntoViewRequester.bringIntoView()
                                delay(300)
                                awaitFrame()
                                bringIntoViewRequester.bringIntoView()
                                //Log.d("IME", "Visible - $count")
                            }
                            delay(300)
                            Log.d("CustomTextField", "End + $id")
                            job.value = null
                        }
                    }*/
                }
            }.then(modifier)
        ,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        isError = isError,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        interactionSource = interactionSource,
        shape = shape,
        colors = colors
    )
}