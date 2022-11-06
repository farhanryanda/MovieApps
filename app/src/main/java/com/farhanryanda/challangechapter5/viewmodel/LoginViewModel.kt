package com.farhanryanda.challangechapter5.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.farhanryanda.challangechapter5.model.UserPreferences
import kotlinx.coroutines.launch

class LoginViewModel(private val pref: com.farhanryanda.challangechapter5.datastore.LoginDataStoreManager): ViewModel() {
    fun getUser(): LiveData<UserPreferences> {
        return pref.getUser().asLiveData()
    }

    fun setUserLogin(isLogin: Boolean) {
        viewModelScope.launch {
            pref.setUserLogin(isLogin)
        }
    }

    fun saveUser(id: String,
                 name: String,
                 username: String,
                 password: String,
                 age: String,
                 address: String) {
        viewModelScope.launch {
            pref.setUser(id,name, username, password, age, address)
        }
    }
}