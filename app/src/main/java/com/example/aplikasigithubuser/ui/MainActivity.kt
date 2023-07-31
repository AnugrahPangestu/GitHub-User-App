package com.example.aplikasigithubuser.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.aplikasigithubuser.R
import com.example.aplikasigithubuser.helper.SettingPreference
import com.example.aplikasigithubuser.helper.SettingViewModel
import com.example.aplikasigithubuser.helper.SettingViewModelFactory
import com.example.aplikasigithubuser.utils.DarkMode

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pref = SettingPreference.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(
            this,
            SettingViewModelFactory(pref)
        )[SettingViewModel::class.java]

        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            DarkMode.setDarkMode(isDarkModeActive)
        }
    }
}