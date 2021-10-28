package com.wilde.caloriecounter2.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.preference.PreferenceFragmentCompat
import com.wilde.caloriecounter2.R

private const val LOGGER_TAG = "Settings Fragment"

class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == "homeScreen") {
            // Translate fragment text name into resource id int using reflection, since
            // listPreference setting can only be a string array (not int)
            val homeScreenField = R.id::class.java.getField(sharedPreferences.getString("homeScreen", "")!!)
            val homeFragment = homeScreenField.get(null) as Int

            // Not sure if this is actually necessary
            //findNavController().graph.startDestination = homeFragment

        }
    }
}
