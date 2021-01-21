package com.wilde.caloriecounter2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.*
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.google.android.material.navigation.NavigationView

private const val ACTIVITY_TAG = "Main Activity"

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(ACTIVITY_TAG, "ACTIVITY CREATED")

        setContentView(R.layout.activity_main)

        val navController = this.findNavController(R.id.nav_host_fragment)
        val navGraph = navController.navInflater.inflate(R.navigation.main_navigation)

        //
        val homeScreen = PreferenceManager.getDefaultSharedPreferences(this).getString("homeScreen", null)
        Log.d(ACTIVITY_TAG, "Preference homeScreen: ${homeScreen ?: "NULL"}")
        if (homeScreen != null) {
            val field = R.id::class.java.getField(homeScreen)
            val homeFragment = field.get(null) as Int
            navGraph.startDestination = homeFragment
        } else {
            navGraph.startDestination = R.id.journalFragment
        }
        navController.graph = navGraph

        /*when (homeScreen) {
            "journalFragment" -> navGraph.startDestination = R.id.journalFragment
            "foodFragment" -> navGraph.startDestination = R.id.foodFragment
            "statisticsFragment" -> navGraph.startDestination = R.id.statisticsFragment
            else -> navGraph.startDestination = R.id.journalFragment
        }*/
        //navGraph.startDestination = homeScreen//R.id.journalFragment
        //navController.graph = navGraph

        // Fix for when app is first launched, title is not changing to nav map item title
        title = navController.currentDestination?.label

        //navController.backStack.cle

        /*val drawerNavigationView = findViewById<NavigationView>(R.id.nav_view)

        val navGraphIds = listOf(R.navigation.journal, R.navigation.food)

        val controller = drawerNavigationView.setupWithNavController(setOf()
        )*/


        //(supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController

        //val navController = findNavController(R.id.nav_host_fragment)
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        //val appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.journalFragment, R.id.foodFragment, R.id.statisticsFragment), drawerLayout)

        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        setSupportActionBar(toolbar)

        toolbar.setupWithNavController(navController, appBarConfiguration)
        //supportActionBar?.hide()


        val navView = findViewById<NavigationView>(R.id.nav_view)

        navView.setupWithNavController(navController)
        navView.setNavigationItemSelectedListener(this)


        /*if (savedInstanceState == null) {
            val fragment = JournalFragment()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.main_content, fragment)
                .commit()
        }*/
    }

    override fun onBackPressed() {
        val navDrawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (navDrawer.isDrawerOpen(GravityCompat.START)) {
            navDrawer.close()
        } else {
            val homeScreen = PreferenceManager.getDefaultSharedPreferences(this).getString("homeScreen", null)

            Log.d(ACTIVITY_TAG, "Preference homeScreen: ${homeScreen?: "NULL"}")

            val homeFragment =
                    if (homeScreen != null) {
                        val homeScreenField = R.id::class.java.getField(homeScreen)
                        homeScreenField.get(null) as Int
                    } else {
                        R.id.journalFragment
                    }

            val navController = findNavController(R.id.nav_host_fragment)
            if (supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.childFragmentManager?.backStackEntryCount == 0 && navController.currentDestination!!.id != homeFragment) {
                navController.navigate(homeFragment, null, NavOptions.Builder().setPopUpTo(navController.currentDestination!!.id, true).build())
            } else {
                super.onBackPressed()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.settingsFragment) {
            findNavController(R.id.nav_host_fragment).navigate(item.itemId, null)
            true//item.onNavDestinationSelected(navController)
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Log.d(ACTIVITY_TAG, "NAV DRAWER OPTIONS MENU SELECTED")
        findNavController(R.id.nav_host_fragment).navigate(item.itemId, null)
        findViewById<DrawerLayout>(R.id.drawer_layout).close()
        return true
    }
}