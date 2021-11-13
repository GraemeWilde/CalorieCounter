package com.wilde.caloriecounter2.composables

import android.util.Log
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import com.squareup.moshi.Moshi
import com.wilde.caloriecounter2.R
import com.wilde.caloriecounter2.composables.other.*
import com.wilde.caloriecounter2.composables.screens.Food
import com.wilde.caloriecounter2.composables.screens.FoodList
import com.wilde.caloriecounter2.composables.screens.Meal
import com.wilde.caloriecounter2.composables.screens.MealList
import com.wilde.caloriecounter2.data.food.entities.Product
import com.wilde.caloriecounter2.data.meals.entities.MealAndComponentsAndFoods
import com.wilde.caloriecounter2.viewmodels.FoodSearchViewModel
import com.wilde.caloriecounter2.viewmodels.FoodViewModel2
import com.wilde.caloriecounter2.viewmodels.MealListViewModel
import com.wilde.caloriecounter2.viewmodels.MealViewModel


sealed interface CalorieNavigation2Interface {
    val route: String
    val title: String
    val argumentsRoute: String?
    val arguments: List<NamedNavArgument>
    val deepLinks: List<NavDeepLink>
    val baseRoute: Boolean
    //val content: @Composable NavHostController.(NavBackStackEntry, MutableState<(@Composable RowScope.() -> Unit)?>) -> Unit

    @Composable
    fun Content(
        nav: NavHostController,
        backStackEntry: NavBackStackEntry,
        actions: ((ActionsScope.() -> Unit)?) -> Unit
        //actions: MutableState<(@Composable RowScope.() -> Unit)?>
    )
}

object CalorieNavigation2 {
    object FoodList: CalorieNavigation2Interface {
        override val route: String = "foodlist"
        override val title: String = "Food List"
        override val argumentsRoute: String? = null
        override val arguments: List<NamedNavArgument> = emptyList()
        override val deepLinks: List<NavDeepLink> = emptyList()
        override val baseRoute: Boolean = true

        @Composable
        override fun Content(
            nav: NavHostController,
            backStackEntry: NavBackStackEntry,
            actions: ((ActionsScope.() -> Unit)?) -> Unit
        ) {
            Log.d("CalorieNavigation", this.title)

            RunOnce {
                actions {
                    actionSearchable(
                        {
                            Icon(
                                painterResource(id = R.drawable.search_internet),
                                stringResource(id = R.string.search_openfoodfacts_hint)
                            )
                        },

                        StringLike.Resource(R.string.search_openfoodfacts_hint),
                        Priority.IfSpace()
                    ) {
                        if (it.isNotEmpty())
                            nav.navigate("food_search/$it")
                    }
                    actionSearchable(
                        { Icon(Icons.Filled.FilterList, null) },
                        StringLike.String("Filter"),
                        Priority.AlwaysShow()
                    ) {}
                    actionButton(
                        { Icon(Icons.Filled.Save, "Save") },
                        StringLike.String("Save"),
                        Priority.InMoreSettings
                    ) {}
                    actionButton(
                        { Icon(Icons.Filled.SaveAlt, "Save") },
                        StringLike.String("Save2"),
                        Priority.InMoreSettings
                    ) {}
                    actionButton(
                        { Icon(Icons.Filled.SavedSearch, "Save") },
                        StringLike.String("Save3"),
                        Priority.InMoreSettings
                    ) {}
                }
            }


            FoodList {
                // OnClick on a food
                val moshi = Moshi.Builder().build()
                val jsonAdapter = moshi.adapter(Product::class.java)
                val food = jsonAdapter.toJson(it)

                nav.navigate("food/$food")
            }
        }
    }

    @Suppress("UNUSED")
    object Food: CalorieNavigation2Interface {
        override val route: String = "food"
        override val title: String = "Food"
        override val argumentsRoute: String = "{foodId}"
        override val arguments: List<NamedNavArgument> = listOf(navArgument("foodId") { type = NavType.StringType })
        override val deepLinks: List<NavDeepLink> = emptyList()
        override val baseRoute: Boolean = false

        @Composable
        override fun Content(
            nav: NavHostController,
            backStackEntry: NavBackStackEntry,
            actions: ((ActionsScope.() -> Unit)?) -> Unit
        ) {
            Log.d("CalorieNavigation", this.title)
            val foodViewModel: FoodViewModel2 = hiltViewModel()

            rememberSaveable {
                val foodString = backStackEntry.arguments!!.getString("foodId")!!

                val moshi = Moshi.Builder().build()
                val food = moshi.adapter(Product::class.java).fromJson(foodString)!!

                foodViewModel.openProduct(food)
                0
            }



            RunOnce {
                actions {
                    actionButton(
                        {
                            Icon(
                                Icons.Filled.Save,
                                stringResource(id = R.string.save)
                            )
                        },
                        StringLike.Resource(R.string.save),
                        Priority.AlwaysShow()
                    ) {
                        foodViewModel.save()
                        nav.popBackStack("foodlist", false)
                    }
//                    IconButton(onClick = {
//                        foodViewModel.save()
//                        nav.popBackStack("foodlist", false)
//                    }) {
//                        Icon(
//                            Icons.Filled.Save,
//                            stringResource(id = R.string.save)
//                        )
//                    }
                }
            }

            Food(foodViewModel)
        }
    }

    @Suppress("UNUSED")
    object MealList: CalorieNavigation2Interface {
        override val route: String = "meallist"
        override val title: String = "Meal List"
        override val argumentsRoute: String? = null
        override val arguments: List<NamedNavArgument> = emptyList()
        override val deepLinks: List<NavDeepLink> = emptyList()
        override val baseRoute: Boolean = true

        @Composable
        override fun Content(
            nav: NavHostController,
            backStackEntry: NavBackStackEntry,
            actions: ((ActionsScope.() -> Unit)?) -> Unit
        ) {
            Log.d("CalorieNavigation", this.title)
            val mealListViewModel: MealListViewModel = hiltViewModel()

            MealList(mealListViewModel) {
                val moshi = Moshi.Builder().build()
                val jsonAdapter = moshi.adapter(MealAndComponentsAndFoods::class.java)
                val mealParam = jsonAdapter.toJson(it)

                nav.navigate("meal/$mealParam")
            }
        }
    }

    object Meal: CalorieNavigation2Interface {
        override val route: String = "meal"
        override val title: String = "Meal"
        override val argumentsRoute: String = "{mealId}"
        override val arguments: List<NamedNavArgument> =
            listOf(navArgument("mealId") { type = NavType.StringType })
        override val deepLinks: List<NavDeepLink> = emptyList()
        override val baseRoute: Boolean = false

        @Composable
        override fun Content(
            nav: NavHostController,
            backStackEntry: NavBackStackEntry,
            actions: ((ActionsScope.() -> Unit)?) -> Unit
        ) {
            Log.d("CalorieNavigation", this.title)
            val mealViewModel: MealViewModel = hiltViewModel()

            val mealString = backStackEntry.arguments!!.getString("mealId")!!

            val moshi = Moshi.Builder().build()
            val meal = moshi.adapter(MealAndComponentsAndFoods::class.java).fromJson(mealString)!!

            mealViewModel.openMeal(meal)

            Meal(mealViewModel)
        }
    }

    @Suppress("UNUSED")
    object FoodSearch: CalorieNavigation2Interface {
        override val route: String = "food_search"
        override val title: String = "Food Search"
        override val argumentsRoute: String = "{search_term}"
        override val arguments: List<NamedNavArgument> =
            listOf(navArgument("search_term") { type = NavType.StringType })
        override val deepLinks: List<NavDeepLink> = emptyList()
        override val baseRoute: Boolean = false

        @Composable
        override fun Content(
            nav: NavHostController,
            backStackEntry: NavBackStackEntry,
            actions: ((ActionsScope.() -> Unit)?) -> Unit
        ) {
            Log.d("CalorieNavigation", this.title)
            val searchViewModel: FoodSearchViewModel = hiltViewModel()

            val searchTerm = backStackEntry.arguments!!.getString("search_term")!!

            if (searchTerm != searchViewModel.queryText.value) {
                searchViewModel.search(searchTerm)
            }

            FoodList(searchViewModel) {
                val moshi = Moshi.Builder().build()
                val jsonAdapter = moshi.adapter(Product::class.java)
                val food = jsonAdapter.toJson(it)

                nav.navigate("food/$food")
            }
        }
    }

    val screens = CalorieNavigation2Interface::class.sealedSubclasses.map { it.objectInstance!! }
}



//class CalorieNavigation {
//    //class argument(val argumentName: String, val )
//    class NavigationDestination(
//        val route: String,
//        val title: String,
//        val argumentsRoute: String? = null,
//        val arguments: List<NamedNavArgument> = emptyList(),
//        val deepLinks: List<NavDeepLink> = emptyList(),
//        val actions: (@Composable RowScope.(NavHostController) -> Unit)? = null,
//        val baseRoute: Boolean = false,
//        val content: @Composable NavHostController.(NavBackStackEntry, MutableState<(@Composable RowScope.() -> Unit)?>) -> Unit
//    )
//
//    class Nav(val navigate: (route: String) -> Unit)
//
//    private val doSave = false
//
//    @ExperimentalComposeUiApi
//    val foodlist = NavigationDestination(
//        "foodlist",
//        "Food List",
//        baseRoute = true
//    ) { backStackEntry, actions ->
//        //val foodListViewModel: FoodListViewModel = hiltViewModel()
//
//        LaunchedEffect(key1 = true) {
//            actions.value = {
//                var searchOpen by remember { mutableStateOf(false) }
//                if (searchOpen) {
//
//                    var searchText by remember { mutableStateOf("") }
//
//                    TextField(
//                        value = searchText,
//                        onValueChange = { searchText = it },
//                        keyboardOptions = KeyboardOptions(
//                            imeAction = ImeAction.Search
//                        ),
//                        keyboardActions = KeyboardActions(
//                            onSearch = {
//                                navigate("food_search/$searchText")
//                            }
//                        )
//                    )
//                } else {
//                    IconButton(onClick = { searchOpen = true }) {
//                        Icon(
//                            painterResource(id = R.drawable.search_internet),
//                            stringResource(id = R.string.search_openfoodfacts_hint)
//                        )
//                    }
//                }
//            }
//        }
//
//
//        FoodList(/*foodListViewModel*/) {
//            val moshi = Moshi.Builder().build()
//            val jsonAdapter = moshi.adapter(Product::class.java)
//            val food = jsonAdapter.toJson(it)
//
//            navigate("food/$food")
//            //navigate("food/$food")
//        }
//    }
//
//    val food = NavigationDestination(
//        "food",
//        "Food",
//        argumentsRoute = "{foodId}",
//        arguments = listOf(navArgument("foodId") { type = NavType.StringType }),
//    ) { backStackEntry, actions ->
//        val foodViewModel: FoodViewModel2 = hiltViewModel()
//
//        val foodString = backStackEntry.arguments!!.getString("foodId")!!
//
//        val moshi = Moshi.Builder().build()
//        val food = moshi.adapter(Product::class.java).fromJson(foodString)!!
//
//        foodViewModel.openProduct(food)
//
//        LaunchedEffect(key1 = true) {
//            actions.value = {
//                IconButton(onClick = {
//                    foodViewModel.save()
//                    popBackStack("foodlist", false)
//                }) {
//                    Icon(
//                        Icons.Filled.Save,
//                        stringResource(id = R.string.save)
//                    )
//                }
//            }
//        }
//
//        Food(foodViewModel)
//    }
//
//    val meallist = NavigationDestination(
//        "meallist",
//        "Meal List",
//        baseRoute = true
//    ) { backStackEntry, actions ->
//        val mealListViewModel: MealListViewModel = hiltViewModel()
//
//        MealList(mealListViewModel) {
//            val moshi = Moshi.Builder().build()
//            val jsonAdapter = moshi.adapter(MealAndComponentsAndFoods::class.java)
//            val mealParam = jsonAdapter.toJson(it)
//
//            navigate("meal/$mealParam")
//        }
//    }
//
//    val meal = NavigationDestination(
//        "meal",
//        "Meal",
//        argumentsRoute = "{mealId}",
//        arguments = listOf(navArgument("mealId") { type = NavType.StringType })
//    ) { backStackEntry, actions ->
//        val mealViewModel: MealViewModel = hiltViewModel()
//
//        val mealString = backStackEntry.arguments!!.getString("mealId")!!
//
//        val moshi = Moshi.Builder().build()
//        val meal = moshi.adapter(MealAndComponentsAndFoods::class.java).fromJson(mealString)!!
//
//        mealViewModel.openMeal(meal)
//
//        Meal(mealViewModel)
//    }
//
//    val foodSearch = NavigationDestination(
//        "food_search",
//        "Food Search",
//        argumentsRoute = "{search_term}",
//        arguments = listOf(navArgument("search_term") { type = NavType.StringType }),
//    ) { backStackEntry, actions ->
//        val searchViewModel: FoodSearchViewModel = hiltViewModel()
//
//        val searchTerm = backStackEntry.arguments!!.getString("search_term")!!
//
//        if (searchTerm != searchViewModel.queryText.value) {
//            searchViewModel.search(searchTerm)
//        }
//
//        FoodList(searchViewModel) {
//            val moshi = Moshi.Builder().build()
//            val jsonAdapter = moshi.adapter(Product::class.java)
//            val food = jsonAdapter.toJson(it)
//
//            navigate("food/$food")
//        }
//    }
//
//    @ExperimentalComposeUiApi
//    val destinations = listOf(
//        foodlist,
//        meallist,
//        food,
//        meal,
//        foodSearch,
//    )
//}