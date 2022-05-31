package com.example.diseaseprediction.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diseaseprediction.model.AuthorizationModel
import com.example.diseaseprediction.model.Preference
import kotlinx.coroutines.launch

class LoginViewModel(private val pref: Preference) : ViewModel() {
    fun saveAuthorization(authorization: AuthorizationModel) {
        viewModelScope.launch {
            pref.saveAuthorization(authorization)
        }
    }

    fun login() {
        viewModelScope.launch {
            pref.login()
        }
    }
}