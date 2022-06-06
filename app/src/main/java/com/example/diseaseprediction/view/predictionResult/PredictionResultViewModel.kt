package com.example.diseaseprediction.view.predictionResult

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.diseaseprediction.api.MlApiConfig
import com.example.diseaseprediction.api.responses.PredictionResponse
import com.example.diseaseprediction.api.responses.ResultItem
import com.example.diseaseprediction.model.Preference
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PredictionResultViewModel(private val pref: Preference) : ViewModel() {
    private val _predictionResponse = MutableLiveData<List<ResultItem>>()
    val predictionResponse: LiveData<List<ResultItem>> = _predictionResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun predict(desc: String) {
        _isLoading.value = true
        val jsonObject = JSONObject().put("desc", desc)
        val requestBody =
            jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())

        val client = MlApiConfig.getMlApiService().predict(requestBody)
        client.enqueue(object : Callback<PredictionResponse> {
            override fun onResponse(
                call: Call<PredictionResponse>,
                response: Response<PredictionResponse>
            ) {
                Log.d("tokenresponse", response.toString())
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _predictionResponse.value = responseBody.result
                        Log.d("okhttp prediction", _predictionResponse.toString())
                    }
                    _isLoading.value = false
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                    _isLoading.value = false
                }
            }

            override fun onFailure(call: Call<PredictionResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: get new token fail")
                _isLoading.value = false
            }
        })
    }
}