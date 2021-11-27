package com.wilde.caloriecounter2.composables

import android.util.Log
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.material.icons.filled.SavedSearch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import com.squareup.moshi.Moshi
import com.wilde.caloriecounter2.R
import com.wilde.caloriecounter2.composables.other.ActionsScope
import com.wilde.caloriecounter2.composables.other.Priority
import com.wilde.caloriecounter2.composables.other.StringLike
import com.wilde.caloriecounter2.composables.screens.Food
import com.wilde.caloriecounter2.composables.screens.FoodList
import com.wilde.caloriecounter2.composables.screens.Meal
import com.wilde.caloriecounter2.composables.screens.MealList
import com.wilde.caloriecounter2.data.food.entities.Product
import com.wilde.caloriecounter2.data.meals.entities.MealAndComponentsAndFoods
import com.wilde.caloriecounter2.viewmodels.FoodSearchViewModel
import com.wilde.caloriecounter2.viewmodels.FoodViewModel
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
        actions: @Composable ((ActionsScope.() -> Unit)?) -> Unit
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
            actions: @Composable ((ActionsScope.() -> Unit)?) -> Unit
        ) {
            Log.d("CalorieNavigation", this.title)

            // Has to be run once otherwise navigation transitions cause it to be run multiple times
            // and sometimes the wrong screen action bar ends up showing
            //RunOnce {
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
            //}


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
            actions: @Composable ((ActionsScope.() -> Unit)?) -> Unit
        ) {
            Log.d("CalorieNavigation", this.title)
            val foodViewModel: FoodViewModel = hiltViewModel()

            rememberSaveable {
                val foodString = backStackEntry.arguments!!.getString("foodId")!!

                val moshi = Moshi.Builder().build()
                val food = moshi.adapter(Product::class.java).fromJson(foodString)!!

                foodViewModel.openProduct(food)
                0
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
            }
            //}

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
            actions: @Composable ((ActionsScope.() -> Unit)?) -> Unit
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
            actions: @Composable ((ActionsScope.() -> Unit)?) -> Unit
        ) {
            Log.d("CalorieNavigation", this.title)
            val mealViewModel: MealViewModel = hiltViewModel()

            val mealString = backStackEntry.arguments!!.getString("mealId")!!

            val moshi = Moshi.Builder().build()
            val meal = moshi.adapter(MealAndComponentsAndFoods::class.java).fromJson(mealString)!!

            mealViewModel.openMeal(meal)

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
            actions: @Composable ((ActionsScope.() -> Unit)?) -> Unit
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