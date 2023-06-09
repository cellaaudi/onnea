package com.cellaaudi.onnea.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LoginPreferencesViewModel(private val pref: LoginPreferences) : ViewModel() {

    fun getLoginUser() : LiveData<Boolean> {
        return pref.getLoginUser().asLiveData()
    }

    fun saveLoginUser(isUserLoggedIn: Boolean) {
        viewModelScope.launch {
            pref.saveLoginUser(isUserLoggedIn)
        }
    }

    fun getToken(): LiveData<String?> {
        return pref.getToken().asLiveData()
    }

    fun saveToken(token: String) {
        viewModelScope.launch {
            pref.saveToken(token)
        }
    }

    fun deleteToken() {
        viewModelScope.launch {
            pref.deleteToken()
        }
    }
}