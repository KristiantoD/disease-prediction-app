package com.example.diseaseprediction.view.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.diseaseprediction.model.AuthorizationModel
import com.example.diseaseprediction.model.Preference

class MainViewModel(private val pref: Preference) : ViewModel() {
    fun authorize(): LiveData<AuthorizationModel> {
        return pref.authorize().asLiveData()
    }
}