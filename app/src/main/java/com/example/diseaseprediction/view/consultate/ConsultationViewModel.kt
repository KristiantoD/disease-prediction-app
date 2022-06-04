package com.example.diseaseprediction.view.consultate

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.*
import com.example.diseaseprediction.api.MlApiConfig
import com.example.diseaseprediction.api.responses.ResultItem
import com.example.diseaseprediction.model.AuthorizationModel
import com.example.diseaseprediction.model.Preference
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConsultationViewModel(private val pref: Preference) : ViewModel() {
    private val _predictionResponse = MutableLiveData<List<ResultItem>>()
    val predictionResponse: LiveData<List<ResultItem>> = _predictionResponse

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

    fun predict(desc: String) {
        _isLoading.value = true
        val jsonObject = JSONObject().put("desc", desc)
        val requestBody =
            jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())

        val client = MlApiConfig.getMlApiService().predict(requestBody)
        client.enqueue(object : Callback<List<ResultItem>> {
            override fun onResponse(
                call: Call<List<ResultItem>>,
                response: Response<List<ResultItem>>
            ) {
                Log.d("tokenresponse", response.toString())
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _predictionResponse.value = response.body()
                    }
                    _isLoading.value = false
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                    _isLoading.value = false
                }
            }

            override fun onFailure(call: Call<List<ResultItem>>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: get new token fail")
                _isLoading.value = false
            }
        })
    }
}