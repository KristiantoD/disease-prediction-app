package com.example.diseaseprediction.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.diseaseprediction.model.Preference
import com.example.diseaseprediction.view.consultate.ConsultationViewModel
import com.example.diseaseprediction.view.detail.DetailViewModel
import com.example.diseaseprediction.view.diseases.DiseaseViewModel
import com.example.diseaseprediction.view.information.InfoViewModel
import com.example.diseaseprediction.view.login.LoginViewModel
import com.example.diseaseprediction.view.medicines.MedicineViewModel
import com.example.diseaseprediction.view.navigation.NavigationViewModel
import com.example.diseaseprediction.view.predictionResult.PredictionResultViewModel
import com.example.diseaseprediction.view.welcome.MainViewModel

class ViewModelFactory(private val pref: Preference) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(pref) as T
            }
            modelClass.isAssignableFrom(NavigationViewModel::class.java) -> {
                NavigationViewModel(pref) as T
            }
            modelClass.isAssignableFrom(ConsultationViewModel::class.java) -> {
                ConsultationViewModel(pref) as T
            }
            modelClass.isAssignableFrom(PredictionResultViewModel::class.java) -> {
                PredictionResultViewModel(pref) as T
            }
            modelClass.isAssignableFrom(DiseaseViewModel::class.java) -> {
                DiseaseViewModel(pref) as T
            }
            modelClass.isAssignableFrom(MedicineViewModel::class.java) -> {
                MedicineViewModel(pref) as T
            }
            modelClass.isAssignableFrom(InfoViewModel::class.java) -> {
                InfoViewModel(pref) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(pref) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(pref) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}