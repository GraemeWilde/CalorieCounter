package com.wilde.caloriecounter2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.core.view.WindowCompat
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
    onSurface = white,
    error = Color(0xFFB00020), //0xFFf5a620
    onError = white
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
    onSurface = black,
    error = Color(0xFFB00020), //0xFFf5a620
    onError = white
)



@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set app to use full screen size (under status bar/navigation bar)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            MaterialTheme(
                colors = if (isSystemInDarkTheme()) darkColors else lightColors,
                typography = Typography(),
                shapes = Shapes()
            ) {

                @OptIn(ExperimentalTextApi::class)
                CompositionLocalProvider(
                    LocalTextStyle provides LocalTextStyle.current.merge(
                        TextStyle(
                            platformStyle = PlatformTextStyle(includeFontPadding = false),
                            lineHeightStyle = LineHeightStyle(
                                alignment = LineHeightStyle.Alignment.Center,
                                trim = LineHeightStyle.Trim.None
                            )
                        )
                    )
                ) {
                    CalorieCounterApp()
                }
            }
        }
    }
}