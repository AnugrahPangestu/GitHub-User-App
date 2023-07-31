package com.example.aplikasigithubuser.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.aplikasigithubuser.R
import com.example.aplikasigithubuser.helper.SettingPreference
import com.example.aplikasigithubuser.helper.SettingViewModel
import com.example.aplikasigithubuser.helper.SettingViewModelFactory
import com.example.aplikasigithubuser.utils.DarkMode
import com.google.android.material.switchmaterial.SwitchMaterial

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        val switchTheme = findViewById<SwitchMaterial>(R.id.sw_theme)

        val pref = SettingPreference.getInstance(dataStore)

        val mainViewModel = ViewModelProvider(
            this,
            SettingViewModelFactory(pref)
        )[SettingViewModel::class.java]

        mainViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            DarkMode.setDarkMode(isDarkModeActive)
            switchTheme.isChecked = isDarkModeActive
        }
        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            mainViewModel.saveThemeSetting(isChecked)
        }
    }
}