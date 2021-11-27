package com.wilde.caloriecounter2

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import com.wilde.caloriecounter2.composables.CalorieCounterApp
import com.wilde.caloriecounter2.composables.other.TextField
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

        // Set app to use full screen size (under status bar/navigation bar)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        // For Accompanist. IME padding functions don't work on some devices without this
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        setContent {
            MaterialTheme(
                colors = if (isSystemInDarkTheme()) darkColors else lightColors,
                typography = Typography(),
                shapes = Shapes()
            ) {
                // Allows use of things related to status bar, navigation bar, and ime paddings
                ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
                    CalorieCounterApp()
                }
            }
        }
    }
}