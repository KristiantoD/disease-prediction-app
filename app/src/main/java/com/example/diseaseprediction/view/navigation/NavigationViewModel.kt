package com.example.diseaseprediction.view.navigation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.diseaseprediction.model.AuthorizationModel
import com.example.diseaseprediction.model.Preference
import kotlinx.coroutines.launch

class NavigationViewModel(private val pref: Preference) : ViewModel() {
    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }

    fun authorize(): LiveData<AuthorizationModel> {
        return pref.authorize().asLiveData()
    }
}