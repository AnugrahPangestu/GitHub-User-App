package com.example.aplikasigithubuser.helper

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class SettingViewModel(private val preference: SettingPreference) : ViewModel(){
    fun getThemeSettings(): LiveData<Boolean> {
        return preference.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            preference.saveThemeSetting(isDarkModeActive)
        }
    }
}