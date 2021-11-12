package com.wilde.caloriecounter2.composables.other

import android.annotation.SuppressLint
import androidx.compose.runtime.*
import androidx.compose.runtime.remember


@SuppressLint("RememberReturnType")
@Composable
inline fun RunOnce(calculation: @DisallowComposableCalls () -> Unit) = remember {
    calculation()
}


@SuppressLint("RememberReturnType")
@Composable
inline fun RunOnce(
    key1: Any?,
    calculation: @DisallowComposableCalls () -> Unit
) = remember(key1) {
    calculation()
}

/**
 * Remember the value returned by [calculation] if [key1] and [key2] are equal to the previous
 * composition, otherwise produce and remember a new value by calling [calculation].
 */
@SuppressLint("RememberReturnType")
@Composable
inline fun RunOnce(
    key1: Any?,
    key2: Any?,
    calculation: @DisallowComposableCalls () -> Unit
) = remember(key1, key2) {
    calculation()
}

/**
 * Remember the value returned by [calculation] if [key1], [key2] and [key3] are equal to the
 * previous composition, otherwise produce and remember a new value by calling [calculation].
 */
@SuppressLint("RememberReturnType")
@Composable
inline fun RunOnce(
    key1: Any?,
    key2: Any?,
    key3: Any?,
    calculation: @DisallowComposableCalls () -> Unit
) = remember(key1, key2, key3) {
    calculation()
}

/**
 * Remember the value returned by [calculation] if all values of [keys] are equal to the previous
 * composition, otherwise produce and remember a new value by calling [calculation].
 */
@SuppressLint("RememberReturnType")
@Composable
inline fun RunOnce(
    vararg keys: Any?,
    calculation: @DisallowComposableCalls () -> Unit
) = remember (*keys) {
    calculation()
}