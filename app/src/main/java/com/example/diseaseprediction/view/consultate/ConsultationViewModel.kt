package com.example.diseaseprediction.view.consultate

import androidx.lifecycle.*
import com.example.diseaseprediction.model.AuthorizationModel
import com.example.diseaseprediction.model.Preference
import kotlinx.coroutines.launch

class ConsultationViewModel(private val pref: Preference) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }

    fun authorize(): LiveData<AuthorizationModel> {
        return pref.authorize().asLiveData()
    }
}