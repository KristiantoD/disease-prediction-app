package com.example.diseaseprediction.view.information

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.diseaseprediction.model.AuthorizationModel
import com.example.diseaseprediction.model.Preference

class InfoViewModel(private val pref: Preference) : ViewModel() {
    fun authorize(): LiveData<AuthorizationModel> {
        return pref.authorize().asLiveData()
    }
}