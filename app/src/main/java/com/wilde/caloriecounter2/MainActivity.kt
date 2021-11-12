package com.wilde.caloriecounter2

import android.os.Bundle
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

private val black = Color(0xFFFFFFFF)
private val white = Color(0xFF000000)

private val darkColors = darkColors(
    primary = primary,
    primaryVariant = primaryDark,
    onPrimary = white,
    background = black,
    onBackground = white
)

private val lightColors = lightColors(
    primary = primary,
    primaryVariant = primaryDark,
    onPrimary = white,
    background = white,
    onBackground = black
)


private const val LOGGER_TAG = "Main Activity"
private const val DEFAULT_HOME_SCREEN = R.id.journalFragment

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            MaterialTheme(
                colors = if (isSystemInDarkTheme()) darkColors else lightColors,
                typography = Typography(),
                shapes = Shapes()
                /*colors = colors!!,
                typography = typography!!,
                shapes = shapes!!*/
            ) {
                ProvideWindowInsets {
                    CalorieCounterApp()
                }
            }
        }
    }
}

//@AndroidEntryPoint
//class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        Log.d(LOGGER_TAG, "Activity Created")
//
//        setContentView(R.layout.activity_main)
//
//        val navController = this.findNavController(R.id.nav_host_fragment)
//        val navGraph = navController.navInflater.inflate(R.navigation.main_navigation)
//
//        // Load user setting for home screen
//        val homeScreen = PreferenceManager.getDefaultSharedPreferences(this).getString("homeScreen", null)
//        Log.d(LOGGER_TAG, "Preference homeScreen: ${homeScreen ?: "NULL"}")
//        navGraph.startDestination =
//                if (homeScreen != null) {
//                    // Translate fragment text name into resource id int using reflection, since
//                    // listPreference setting can only be a string array (not int)
//                    val field = R.id::class.java.getField(homeScreen)
//                    val homeFragment = field.get(null) as Int
//                    //navGraph.startDestination =
//                    homeFragment
//                } else {
//                    //navGraph.startDestination =
//                    DEFAULT_HOME_SCREEN
//                }
//        navController.graph = navGraph
//
//        // Fix for when app is first launched, title is not changing to nav map item title
//        title = navController.currentDestination?.label
//
//        // Set up side drawer for navigation
//        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.journalFragment,
//                R.id.foodListFragment,
//                R.id.mealListFragment,
//                R.id.statisticsFragment,
//            ),
//            drawerLayout
//        )
//
//        // Set up toolbar as action bar and also with nav controller for hamburger menu
//        // functionality
//        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
//        setSupportActionBar(toolbar)
//        toolbar.setupWithNavController(navController, appBarConfiguration)
//
//        // Set up the navView fragment container
//        val navView = findViewById<NavigationView>(R.id.nav_view)
//        navView.setupWithNavController(navController)
//
//        // Set up listener for nav drawer item selection
//        navView.setNavigationItemSelectedListener(this)
//    }
//
//    override fun onBackPressed() {
//        // If navDrawer is open, back button closes it
//        val navDrawer = findViewById<DrawerLayout>(R.id.drawer_layout)
//        if (navDrawer.isDrawerOpen(GravityCompat.START)) {
//            navDrawer.close()
//        } else {
//            // Read user settings for home screen
//            val homeScreen = PreferenceManager.getDefaultSharedPreferences(this).getString("homeScreen", null)
//            Log.d(LOGGER_TAG, "Preference homeScreen: ${homeScreen ?: "NULL"}")
//
//            // Translate fragment text name into resource id int using reflection, since
//            // listPreference setting can only be a string array (not int)
//            val homeFragment =
//                    if (homeScreen != null) {
//                        val homeScreenField = R.id::class.java.getField(homeScreen)
//                        homeScreenField.get(null) as Int
//                    } else {
//                        DEFAULT_HOME_SCREEN
//                    }
//
//            val navController = findNavController(R.id.nav_host_fragment)
//
//            // If we are at end of back stack (aka back would close), if we are not on home screen
//            // then go to home screen instead of closing (pressing back once more should now close)
//            if (supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.childFragmentManager?.backStackEntryCount == 0 && navController.currentDestination!!.id != homeFragment) {
//                navController.navigate(homeFragment, null, NavOptions.Builder().setPopUpTo(navController.currentDestination!!.id, true).build())
//            } else {
//                super.onBackPressed()
//            }
//        }
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        Log.d(LOGGER_TAG, "Inflate options menu.")
//        menuInflater.inflate(R.menu.main_menu, menu)
//        return super.onCreateOptionsMenu(menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        Log.d(LOGGER_TAG, "Option menu item selected.")
//        return if (item.itemId == R.id.settingsFragment) {
//            findNavController(R.id.nav_host_fragment).navigate(item.itemId, null)
//            true
//        } else {
//            super.onOptionsItemSelected(item)
//        }
//    }
//
//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        Log.d(LOGGER_TAG, "Nav drawer menu item selected.")
//        findNavController(R.id.nav_host_fragment).navigate(item.itemId, null)
//        findViewById<DrawerLayout>(R.id.drawer_layout).close()
//        return true
//    }
//
//    override fun onNewIntent(intent: Intent?) {
//        if (!(intent != null && handleIntent(intent))) {
//            super.onNewIntent(intent)
//        }
//    }
//
//    private fun handleIntent(intent: Intent): Boolean {
//        if (intent.action == Intent.ACTION_SEARCH) {
//
//            val query = intent.getStringExtra(SearchManager.QUERY)
//            if (query != null) {
//                if (findNavController(R.id.nav_host_fragment).currentDestination?.id != R.id.foodSearchFragment) {
//                    findNavController(R.id.nav_host_fragment).navigate(R.id.foodSearchFragment)
//                }
//                ViewModelProvider(this)[FoodSearchViewModel::class.java].search(query)
//            }
//            return true
//            /*} else {
//                val bundle = bundleOf("search" to intent)
//                //findNavController(R.id.nav_host_fragment).currentDestination
//                findNavController(R.id.nav_host_fragment).navigate(R.id.foodSearchFragment, bundle)
//                return true
//            }*/
//        }
//        return false
//    }
//}