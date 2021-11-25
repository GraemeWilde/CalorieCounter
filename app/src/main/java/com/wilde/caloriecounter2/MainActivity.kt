package com.wilde.caloriecounter2

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import com.wilde.caloriecounter2.composables.CalorieCounterApp
import dagger.hilt.android.AndroidEntryPoint


private val primary = Color(0xFF9C0000)
//private val primaryLight = Color(0xFFD5442B)
private val primaryDark = Color(0xFF670000)

private val white = Color(0xFFF1F1F1)
private val black = Color(0xFF131313)

private val darkColors = darkColors(
    primary = primary,
    onPrimary = white,
    primaryVariant = primaryDark,
    secondary = primary,
    onSecondary = white,
    secondaryVariant = primaryDark,
    background = black,
    onBackground = white,
    surface = Color(0xFF121212),
    onSurface = white
)

private val lightColors = lightColors(
    primary = primary,
    onPrimary = white,
    primaryVariant = primaryDark,
    secondary = primary,
    onSecondary = white,
    secondaryVariant = primaryDark,
    background = white,
    onBackground = black,
    surface = white,
    onSurface = black
)


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)

        setContent {
            MaterialTheme(
                colors = if (isSystemInDarkTheme()) darkColors else lightColors,
                typography = Typography(),
                shapes = Shapes()
            ) {
                ProvideWindowInsets {
                    CalorieCounterApp()
                }
            }
        }
    }
}