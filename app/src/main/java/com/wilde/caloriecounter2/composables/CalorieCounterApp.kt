package com.wilde.caloriecounter2.composables

import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import com.wilde.caloriecounter2.composables.other.*
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch

//@ExperimentalComposeUiApi
//@ExperimentalMaterialApi // Added to use DrawState.targetValue
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CalorieCounterApp() {

    val navController = rememberNavController()
    //val navScreens = remember { CalorieNavigation(/*navController*/) }
    val coroutineScope = rememberCoroutineScope()

    val drawerState = rememberDrawerState(DrawerValue.Closed)

    var title by remember{ mutableStateOf("Titlebar") }
    var baseRoute by remember{ mutableStateOf(true) }
    val actions = remember{ mutableStateOf<(ActionsScope.() -> Unit)?>(null) }


    //val navCon2 by rememberUpdatedState(navController)

    val calNav = remember { CalorieNavigation2 }
    //var counter by remember { mutableStateOf(0) }


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

    Column(
        Modifier
            .statusBarsPadding()
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
                        if (drawerState.targetValue == DrawerValue.Closed) {
                            drawerOpen()
                        } else {
                            drawerClose()
                        }
                    } else {
                        navController.popBackStack()
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
                for(screen in calNav.screens) {
                    if (screen.baseRoute) {
                        Text(
                            screen.title,
                            Modifier
                                .clickable {
                                    drawerClose()
                                    navController.navigate(screen.route)
                                }
                                .fillMaxWidth()
                                .padding(20.dp, 15.dp)
                            ,
                            fontWeight = FontWeight.SemiBold
                        )

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

            //val usCounter = rememberUpdatedState(counter)



            /*LaunchedEffect(key1 = true) {
                Log.d("CalorieCounterApp", "Main Launched Effect Start: $counter")
                delay(5000)
                Log.d("CalorieCounterApp", "Main Launched Effect End: $counter")
            }*/

            // DisposableEffect with key that will never change, to add callback once, and remove
            // when this leaves composition
            DisposableEffect(key1 = true) {
                backDispatcher.addCallback(callback)

                onDispose {
                    callback.remove()
                }
            }

            val drawerCloseCallbackEnabler = @Composable {
                LaunchedEffect(key1 = drawerState.targetValue) {
                    // Enable callback when drawer is open or opening
                    callback.isEnabled = drawerState.targetValue == DrawerValue.Open
                    Log.d("CalorieCounterApp", "DrawerState")
                }
            }

            drawerCloseCallbackEnabler()

            NavHost(
                navController = navController,
                startDestination = CalorieNavigation2.FoodList.route/*navScreens.foodlist.route*/
            ) {
                for(screen in calNav.screens) {
                    //val screen = screen.objectInstance!!
                    composable(
                        screen.argumentsRoute?.let { "${screen.route}/$it" } ?: screen.route,
                        screen.arguments,
                        screen.deepLinks,
                    ) { backStackEntry ->
                        Log.d("CalorieCounterApp", "Compose: ${screen.route}")
                        /*DisposableEffect(key1 = true) {
                            Log.d("CalorieCounterApp", "NavScreenLaunch")
                            baseRoute = screen.baseRoute
                            title = screen.title
                            actions.value = null

                            onDispose {  }
                        }*/

//                        LaunchedEffect(key1 = true) {
//                            //Log.d("CalorieCounterApp", "Launch: ${screen.route}")
//                            Log.d("CalorieCounterApp", "Second")
//                            baseRoute = screen.baseRoute
//                            title = screen.title
//                            actions.value = null
//                        }

                        RunOnce {
                            Log.d("CalorieCounterApp", "Init Screen")
                            baseRoute = screen.baseRoute
                            title = screen.title
                            actions.value = null
                            //Log.d("Operators", "${false || false && true && true}")
                        }

                        screen.Content(
                            navController,
                            backStackEntry
                        ) { actions.value = it }
                    }
                }
                /*for(screen in navScreens.destinations) {
                    composable(
                        //screen.route + (screen.argumentsRoute?.let { "/$it" } ?: ""),
                        screen.argumentsRoute?.let { "${screen.route}/$it" } ?: screen.route,
                        screen.arguments,
                        screen.deepLinks,
                    ) { backStackEntry ->
                        //actions.value = { screen.actions?.invoke(this, navController) }
                        LaunchedEffect(key1 = true) {
                            Log.d("CalorieCounterApp", "NavLaunched")
                            baseRoute = screen.baseRoute
                            title = screen.title
                            actions.value = null
                        }
                        //
                        screen.content(
                            navController,
                            backStackEntry,
                            actions
                        )
                    }
                }*/
            }
        }
    }
}