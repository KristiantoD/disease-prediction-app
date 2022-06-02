package com.example.diseaseprediction.view.medicines

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.*
import com.example.diseaseprediction.api.ApiConfig
import com.example.diseaseprediction.api.responses.ListItemResponse
import com.example.diseaseprediction.api.responses.TokenResponse
import com.example.diseaseprediction.model.AuthorizationModel
import com.example.diseaseprediction.model.Preference
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MedicineViewModel(private val pref: Preference) : ViewModel() {
    private val _allItemResponse = MutableLiveData<List<ListItemResponse>>()
    val allItemResponse: LiveData<List<ListItemResponse>> = _allItemResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _newToken = MutableLiveData<String>()
    private val _client = MutableLiveData<Call<*>>()

    fun authorize(): LiveData<AuthorizationModel> {
        return pref.authorize().asLiveData()
    }

    fun saveToken(token: String) {
        viewModelScope.launch {
            pref.saveToken(token)
        }
    }

    private fun getNewToken(refreshToken: String, type: Int, keyword: String = "") {
        val jsonObject = JSONObject().put("refreshToken", refreshToken)
        val requestBody =
            jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())

        val client = ApiConfig.getApiService().refreshToken(requestBody)
        _client.value = client
        client.enqueue(object : Callback<TokenResponse> {
            override fun onResponse(
                call: Call<TokenResponse>,
                response: Response<TokenResponse>
            ) {
                Log.d("tokenresponse", response.toString())
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        saveToken(responseBody.accessToken)
                        _newToken.value = responseBody.accessToken
                        if (type == 1) {
                            getAllMedicine(_newToken.value ?: "", refreshToken)
                        } else if (type == 2) {
                            searchMedicine(_newToken.value ?: "", refreshToken, keyword)
                        }
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: get new token fail")
            }
        })
    }

    fun getAllMedicine(accessToken: String, refreshToken: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getAllMedicine("Bearer $accessToken")
        _client.value = client
        client.enqueue(object : Callback<List<ListItemResponse>> {
            override fun onResponse(
                call: Call<List<ListItemResponse>>,
                response: Response<List<ListItemResponse>>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _allItemResponse.value = response.body()
                    }
                } else {
                    getNewToken(refreshToken, 1)
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ListItemResponse>>, t: Throwable) {
                Log.e(TAG, "onFailure: get medicine server fail")
                _isLoading.value = false
            }
        })
    }

    fun searchMedicine(accessToken: String, refreshToken: String, keyword: String) {
        _isLoading.value = true
        val jsonObject = JSONObject()
            .put("keyword", keyword)
        val requestBody =
            jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())

        val client = ApiConfig.getApiService().searchMedicine("Bearer $accessToken", requestBody)
        _client.value = client
        client.enqueue(object : Callback<List<ListItemResponse>> {
            override fun onResponse(
                call: Call<List<ListItemResponse>>,
                response: Response<List<ListItemResponse>>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _allItemResponse.value = response.body()
                    }
                } else {
                    getNewToken(refreshToken, 2, keyword)
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ListItemResponse>>, t: Throwable) {
                Log.e(TAG, "onFailure: get medicine server fail")
                _isLoading.value = false
            }
        })
    }

    fun cancelJob() {
        _client.value?.cancel()
    }
}