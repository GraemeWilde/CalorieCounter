package com.wilde.caloriecounter2

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceFragmentCompat

class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onResume() {
        super.onResume()


    }

    @SuppressLint("RestrictedApi")
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        Log.d("SHARED LISTENER", "Key: $key - ${sharedPreferences.getString(key, "")}")
        //Resources.getId

        if (key == "homeScreen") {
            val homeScreenField = R.id::class.java.getField(sharedPreferences.getString("homeScreen", "")!!)

            val homeFragment = homeScreenField.get(null) as Int

            findNavController().graph.startDestination = homeFragment

            /*val navController = findNavController()

            navController.navigate(homeFragment)
            val backstackEntry = navController.backStack.removeLast()
            navController.backStack.addFirst(backstackEntry)

            val destination = navController.graph.getAction(homeFragment)

            //val ClassNavControllerViewModel = NavControllerViewModel::class.java;


            val cls = Class.forName("androidx.navigation.NavControllerViewModel")

            val methodNavBackStackEntry = NavBackStackEntry::class.java.constructors *///.getConstructor(android.content.Context::class.java, NavDestination::class.java, Bundle::class.java, LifecycleOwner::class.java, cls)
            //val methodNavBackStackEntry = NavBackStackEntry::class.java.constructors

            //navController.backStack.addFirst(NavBackStackEntry(requireContext(), navController.graph.findNode(homeFragment)!!, null, null, null))

            //navController.backStack.

            //val dest = navController.currentDestination!!
            //navController.graph.remove(dest)


            //val navGraph = navController.navInflater.inflate(R.navigation.main_navigation)

            //navGraph.startDestination = homeFragment
            //navGraph.addDestinations(navController.graph)

            //val navGraph = findNavController()


            //navGraph.backStack.addFirst(NavBackStackEntry(context, navGraph.navigatorProvider.getNavigator()))
            //val backStackField = NavController::class.java.getField("mBackStack")
            /*backStackField.isAccessible = true
            @Suppress("UNCHECKED_CAST")
            val arrayDeque = backStackField.get(findNavController()) as java.util.ArrayDeque<NavBackStackEntry>
            val graph = arrayDeque.first.destination as NavGraph*/

            //arrayDeque.addFirst(NavBackStackEntry(context, ))





            Log.d("SHARED LISTENER", "Value: $homeFragment")
            //Log.d("SHARED LISTENER", "Journal Frag id: " + R.id.journalFragment)
        }
    }
}
