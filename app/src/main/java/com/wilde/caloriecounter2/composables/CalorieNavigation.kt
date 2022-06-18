package com.wilde.caloriecounter2.composables

import android.util.Log
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import com.squareup.moshi.Moshi
import com.wilde.caloriecounter2.R
import com.wilde.caloriecounter2.composables.other.*
import com.wilde.caloriecounter2.composables.screens.*
import com.wilde.caloriecounter2.data.food.entities.Product
import com.wilde.caloriecounter2.data.journal.entities.FullJournalEntry
import com.wilde.caloriecounter2.data.meals.entities.MealAndComponentsAndFoods
import com.wilde.caloriecounter2.other.parse
import com.wilde.caloriecounter2.other.serialize
import com.wilde.caloriecounter2.viewmodels.*


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
        actions: @Composable ((ActionsScope.() -> Unit)?) -> Unit
        //actions: MutableState<(@Composable RowScope.() -> Unit)?>
    )
}

object CalorieNavigation2 {
    object FoodList : CalorieNavigation2Interface {
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
            actions: @Composable ((ActionsScope.() -> Unit)?) -> Unit
        ) {
            Log.d("CalorieNavigation", this.title)

            // Has to be run once otherwise navigation transitions cause it to be run multiple times
            // and sometimes the wrong screen action bar ends up showing
            //RunOnce {

//            var searchTerm by remember { mutableStateOf("") }
//
//            val launcher = rememberLauncherForActivityResult(
//                ActivityResultContracts.RequestPermission()
//            ) { isGranted ->
//                if (isGranted) {
//                    if (searchTerm != "") {
//                        nav.navigate("food_search/$searchTerm")
//                    }
//                }
//            }

            val context = LocalContext.current

            actions {
                actionSearchable(
                    {
                        Icon(
                            painterResource(id = R.drawable.search_internet),
                            stringResource(id = R.string.search_openfoodfacts_hint)
                        )
                    },

                    StringLike.Resource(R.string.search_openfoodfacts_hint),
                    Priority.AlwaysShow()
                ) {
                    if (it.isNotEmpty()) {
                        nav.navigate("food_search/$it")
//                        Log.d("FoodList", "Permission")
//                        when (PackageManager.PERMISSION_GRANTED) {
//                            ContextCompat.checkSelfPermission(
//                                context,
//                                Manifest.permission.INTERNET
//                            ) -> {
//                                Log.d("FoodList", "Found Permission")
//                                nav.navigate("food_search/$it")
//                            }
//                            else -> {
//                                Log.d("FoodList", "No Permission")
//                                launcher.launch(Manifest.permission.INTERNET)
//                            }
//                        }
                    }
                }
                actionSearchable(
                    { Icon(Icons.Filled.FilterList, null) },
                    StringLike.String("Filter"),
                    Priority.IfSpace()
                ) {}
//                    actionButton(
//                        { Icon(Icons.Filled.Save, "Save") },
//                        StringLike.String("Save"),
//                        Priority.AlwaysShow()
//                    ) {}
//                    actionButton(
//                        { Icon(Icons.Filled.SaveAlt, "Save") },
//                        StringLike.String("Save2"),
//                        Priority.AlwaysShow()
//                    ) {}
//                    actionButton(
//                        { Icon(Icons.Filled.SavedSearch, "Save") },
//                        StringLike.String("Save3"),
//                        Priority.InMoreSettings
//                    ) {}
//                    actionButton(
//                        { Icon(Icons.Filled.SavedSearch, "Save") },
//                        StringLike.String("Save4"),
//                        Priority.AlwaysShow()
//                    ) {}
//                    actionButton(
//                        { Icon(Icons.Filled.SavedSearch, "Save") },
//                        StringLike.String("Save5"),
//                        Priority.AlwaysShow()
//                    ) {}
//                    actionButton(
//                        { Icon(Icons.Filled.Save, "Save") },
//                        StringLike.String("Save6"),
//                        Priority.AlwaysShow()
//                    ) {}
//                    actionButton(
//                        { Icon(Icons.Filled.SaveAlt, "Save") },
//                        StringLike.String("Save7"),
//                        Priority.AlwaysShow()
//                    ) {}
//                    actionButton(
//                        { Icon(Icons.Filled.SavedSearch, "Save") },
//                        StringLike.String("Save8"),
//                        Priority.AlwaysShow()
//                    ) {}
//                    actionButton(
//                        { Icon(Icons.Filled.SavedSearch, "Save") },
//                        StringLike.String("Save9"),
//                        Priority.AlwaysShow()
//                    ) {}
            }
            //}


            FoodList {
                // OnClick on a food
                /*val moshi = Moshi.Builder().build()
                val jsonAdapter = moshi.adapter(Product::class.java)
                val food = jsonAdapter.toJson(it)*/

                //val food = NavArg<Product>(true).serialize(it)

                nav.navigate("food/${it.serialize()}")

                //nav.navigate("food", Pair("food", it))
            }
        }
    }

    @Suppress("UNUSED")
    object Food : CalorieNavigation2Interface {
        override val route: String = "food"
        override val title: String = "Food"
        override val argumentsRoute: String = "{foodId}"
        override val arguments: List<NamedNavArgument> =
            listOf(navArgument("foodId") { type = NavType.StringType })
        override val deepLinks: List<NavDeepLink> = emptyList()
        override val baseRoute: Boolean = false

        @Composable
        override fun Content(
            nav: NavHostController,
            backStackEntry: NavBackStackEntry,
            actions: @Composable ((ActionsScope.() -> Unit)?) -> Unit
        ) {
            Log.d("CalorieNavigation", this.title)
            val foodViewModel: FoodViewModel = hiltViewModel()

            RunOnceSaveable {
                val food = backStackEntry.arguments!!.getString("foodId")!!.parse<Product>()
//                val foodString = backStackEntry.arguments!!.getString("foodId")!!
//
//                val moshi = Moshi.Builder().build()
//                val food = moshi.adapter(Product::class.java).fromJson(foodString)!!

                foodViewModel.openProduct(food)
            }


            //RunOnce {
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
                actionButton(
                    {
                        Icon(
                            Icons.Filled.Print,
                            "Print"
                        )
                    },
                    StringLike.String("Print"),
                    Priority.AlwaysShow()
                ) {
                    Log.d("Food", foodViewModel.product.toProduct().toString())
                }
            }
            //}

            Food(foodViewModel)
        }
    }

    @Suppress("UNUSED")
    object MealList : CalorieNavigation2Interface {
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
            actions: @Composable ((ActionsScope.() -> Unit)?) -> Unit
        ) {
            Log.d("CalorieNavigation", this.title)
            val mealListViewModel: MealListViewModel = hiltViewModel()

            MealListScreen(
                mealListViewModel,
                {
                    val t: MealAndComponentsAndFoods? = null
                    nav.navigate("meal/0")
                }
            ) {
                nav.navigate("meal/${it.serialize()}")
            }
        }
    }

    object Meal : CalorieNavigation2Interface {
        override val route: String = "meal"
        override val title: String = "Meal"
        override val argumentsRoute: String = "{mealId}"
        override val arguments: List<NamedNavArgument> =
            //listOf(navArgument("mealId") { type = NavType.StringType })
            listOf(navArgument("mealId") { type = NavType.StringType })
        override val deepLinks: List<NavDeepLink> = emptyList()
        override val baseRoute: Boolean = false

        @Composable
        override fun Content(
            nav: NavHostController,
            backStackEntry: NavBackStackEntry,
            actions: @Composable ((ActionsScope.() -> Unit)?) -> Unit
        ) {
            Log.d("CalorieNavigation", this.title)
            val mealViewModel: MealViewModel = hiltViewModel()

            RunOnce {
                val mealArg = backStackEntry.arguments!!.getString("mealId") ?: "0"

                if (mealArg != "0") {
                    val meal = mealArg.parse<MealAndComponentsAndFoods>()
                    mealViewModel.openMeal(meal)
                } else {
                    mealViewModel.clear()
                }
                /*val mealString = backStackEntry.arguments!!.getString("mealId")!!

                if (mealString.toIntOrNull() != 0) {
                    val moshi = Moshi.Builder().build()
                    val meal =
                        moshi.adapter(MealAndComponentsAndFoods::class.java).fromJson(mealString)!!

                    mealViewModel.openMeal(meal)
                } else {
                    mealViewModel.clear()
                }*/

            }

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
                    mealViewModel.save()
                    nav.popBackStack()
                    //nav.popBackStack("meallist", false)
                }
            }

            MealScreen(mealViewModel)
        }
    }

    @Suppress("UNUSED")
    object FoodSearch : CalorieNavigation2Interface {
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
            actions: @Composable ((ActionsScope.() -> Unit)?) -> Unit
        ) {
            Log.d("CalorieNavigation", this.title)
            val searchViewModel: FoodSearchViewModel = hiltViewModel()

            val searchTerm = backStackEntry.arguments!!.getString("search_term")!!


            if (searchTerm != searchViewModel.queryText.value) {
                searchViewModel.search(searchTerm)
//                when (PackageManager.PERMISSION_GRANTED) {
//                    ContextCompat.checkSelfPermission(
//                        LocalContext.current,
//                        Manifest.permission.INTERNET
//                    ) -> {
//                        searchViewModel.search(searchTerm)
//                    }
//                    else -> {
//                        nav.popBackStack()
//                    }
//                }
            }

            FoodList(searchViewModel) {
                val moshi = Moshi.Builder().build()
                val jsonAdapter = moshi.adapter(Product::class.java)
                val food = jsonAdapter.toJson(it)

                nav.navigate("food/$food")
            }
        }
    }

    @Suppress("UNUSED")
    object Journal : CalorieNavigation2Interface {
        override val route: String = "journal"
        override val title: String = "Journal"
        override val argumentsRoute: String? = null
        override val arguments: List<NamedNavArgument> = emptyList()
        override val deepLinks: List<NavDeepLink> = emptyList()
        override val baseRoute: Boolean = true

        @Composable
        override fun Content(
            nav: NavHostController,
            backStackEntry: NavBackStackEntry,
            actions: @Composable ((ActionsScope.() -> Unit)?) -> Unit
        ) {
            Journal(
                hiltViewModel(),
                { nav.navigate("journal_entry/0") },
                {
                    nav.navigate("journal_entry/${it.serialize()}")
                }
            )
        }
    }

    @Suppress("UNUSED")
    object JournalEntry : CalorieNavigation2Interface {
        override val route: String = "journal_entry"
        override val title: String = "Journal Entry"
        override val argumentsRoute: String = "{journalId}"
        override val arguments: List<NamedNavArgument> =
            listOf(navArgument("journalId") { type = NavType.StringType })
        override val deepLinks: List<NavDeepLink> = emptyList()
        override val baseRoute: Boolean = false

        @Composable
        override fun Content(
            nav: NavHostController,
            backStackEntry: NavBackStackEntry,
            actions: @Composable ((ActionsScope.() -> Unit)?) -> Unit
        ) {
            val journalEntryViewModel: JournalEntryViewModel = hiltViewModel()

            RunOnce {
                val journalArg = backStackEntry.arguments!!.getString("journalId")!!

                if (journalArg == "0") {
                    journalEntryViewModel.clear()
                } else {
                    val journalEntry = journalArg.parse<FullJournalEntry>()
                    journalEntryViewModel.openJournalEntry(journalEntry)
                }
            }

            JournalEntry(journalEntryViewModel)
        }
    }

    //val screens = CalorieNavigation2Interface::class.sealedSubclasses.map { it.objectInstance!! }

    val screens = listOf(FoodList, Food, MealList, Meal, FoodSearch, Journal, JournalEntry)
}