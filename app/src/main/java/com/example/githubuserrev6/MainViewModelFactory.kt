package com.example.githubuserrev6

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import com.example.githubuserrev6.UserSearchViewModel

class MainViewModelFactory(private val prefSettingTheme: ThemePreferenceSetting) :
    NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserSearchViewModel::class.java)) {
            return UserSearchViewModel(prefSettingTheme) as T
        }
        throw IllegalArgumentException("Error! Unknown ViewModel class : " + modelClass.name)
    }
}