package com.wilde.caloriecounter2.composables

import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import com.wilde.caloriecounter2.composables.other.ActionsRow
import com.wilde.caloriecounter2.composables.other.ActionsScope
import com.wilde.caloriecounter2.composables.other.RunOnce
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch

//@ExperimentalComposeUiApi
//@ExperimentalMaterialApi // Added to use DrawState.targetValue
//@OptIn(ExperimentalMaterialApi::class) // Added to use DrawState.targetValue
@Composable
fun CalorieCounterApp() {

    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()

    val drawerState = rememberDrawerState(DrawerValue.Closed)

    var title by remember{ mutableStateOf("Titlebar") }
    var baseRoute by remember{ mutableStateOf(true) }
    val actions = remember{ mutableStateOf<(ActionsScope.() -> Unit)?>(null) }


    fun drawerOpen() {
        coroutineScope.coroutineContext.cancelChildren()
        coroutineScope.launch {
            drawerState.open()
        }
    }

    fun drawerClose() {
        coroutineScope.coroutineContext.cancelChildren()
        coroutineScope.launch {
            drawerState.close()
        }
    }



    val backDispatcher = LocalOnBackPressedDispatcherOwner.current!!.onBackPressedDispatcher/*) {
        "No back dispatcher"
    }.onBackPressedDispatcher*/

    val insets = LocalWindowInsets.current
    val imeBottom = with(LocalDensity.current) { insets.ime.bottom.toDp() }

    LaunchedEffect(imeBottom) {
        Log.d("IME", imeBottom.toString())
    }

    Column(
        Modifier
            .statusBarsPadding()
            //.padding(bottom = 40.dp)
            .navigationBarsWithImePadding()
    ) {
        TopAppBar(
            { Text(
                title,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            ) },
            navigationIcon = {
                IconButton(onClick = {
                    if (baseRoute) {
                        @OptIn(ExperimentalMaterialApi::class)
                        if (drawerState.targetValue == DrawerValue.Closed) {
                            drawerOpen()
                        } else {
                            drawerClose()
                        }
                    } else {
                        //navController.popBackStack()
                        backDispatcher.onBackPressed()
                    }
                }) {
                    if (baseRoute) {
                        Icon(Icons.Filled.Menu, null)
                    } else {
                        Icon(Icons.Filled.ArrowBack, null)
                    }
                }
            },
            actions = {
                CompositionLocalProvider(LocalContentAlpha provides 0.87f) {
                    ActionsRow {
                        actions.value?.invoke(this)
                    }
                }
            }
        )
        ModalDrawer(
            drawerContent = {
                Log.d("CalorieCounterApp", "Drawer")
                for(screen in CalorieNavigation2.screens) {
                    if (screen.baseRoute) {
                        DropdownMenuItem(
                            onClick = {
                                drawerClose()
                                navController.navigate(
                                    screen.route
                                )
                            }
                        ) {
                            Text(screen.title, fontWeight = FontWeight.SemiBold)
                        }
//                        Text(
//                            screen.title,
//                            Modifier
//                                .clickable {
//                                    drawerClose()
//                                    navController.navigate(
//                                        screen.route
//                                    )/* {
//                                        popUpTo(
//                                            screen.route
//                                        ) {
//                                            inclusive = true
//                                        }
//                                    }*/
//                                }
//                                .fillMaxWidth()
//                                .padding(20.dp, 15.dp)
//                            ,
//                            fontWeight = FontWeight.SemiBold
//                        )

                    }
                }
            },
            drawerState = drawerState
        ) {
            Log.d("CalorieCounterApp", "Main")

            // Remember to avoid creating new callback every recomposition
            val callback = remember {
                object : OnBackPressedCallback(false) {
                    override fun handleOnBackPressed() {
                        drawerClose()
                    }
                }
            }

            // DisposableEffect with key that will never change, to add callback once, and remove
            // when this leaves composition
            DisposableEffect(key1 = true) {
                backDispatcher.addCallback(callback)

                onDispose {
                    callback.remove()
                }
            }

            // Wrap in composable to limit recomposition scope (is this a good thing to do?)
            val drawerCloseCallbackEnabler = @Composable {
                @OptIn(ExperimentalMaterialApi::class)
                LaunchedEffect(key1 = drawerState.targetValue) {
                    // Enable callback when drawer is open or opening
                    callback.isEnabled = drawerState.targetValue == DrawerValue.Open
                    Log.d("CalorieCounterApp", "DrawerState")
                }
            }

            drawerCloseCallbackEnabler()

            NavHost(
                navController = navController,
                startDestination = CalorieNavigation2.FoodList.route
            ) {
                for(screen in CalorieNavigation2.screens) {
                    composable(
                        // Create route from route + route arguments (if there are route arguments)
                        screen.argumentsRoute?.let { "${screen.route}/$it" } ?: screen.route,
                        screen.arguments,
                        screen.deepLinks,
                    ) { backStackEntry ->
                        Log.d("CalorieCounterApp", "Compose: ${screen.route}")

                        // Setup when new screen is opened
                        RunOnce {
                            Log.d("CalorieCounterApp", "Init Screen")
                            baseRoute = screen.baseRoute
                            title = screen.title
                            actions.value = null
                        }

                        screen.Content(
                            navController,
                            backStackEntry
                        ) {
                            // Only set actions once when new screen is opened. Without this issues
                            // occur because of transitions between screens running both previous
                            // and new screens mutliple times
                            RunOnce { actions.value = it }
                        }
                    }
                }
            }
        }
    }
}