package com.example.diseaseprediction.view.detail

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.*
import com.example.diseaseprediction.api.ApiConfig
import com.example.diseaseprediction.api.responses.DetailItemResponseItem
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

class DetailViewModel(private val pref: Preference) : ViewModel() {
    private val _detailItemResponse = MutableLiveData<List<DetailItemResponseItem>>()
    val detailItemResponse: LiveData<List<DetailItemResponseItem>> = _detailItemResponse

    private val _medicineResponse = MutableLiveData<List<ListItemResponse>>()
    val medicineResponse: LiveData<List<ListItemResponse>> = _medicineResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isLoadingMed = MutableLiveData<Boolean>()
    val isLoadingMed: LiveData<Boolean> = _isLoadingMed

    private val _newToken = MutableLiveData<String>()

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }

    fun authorize(): LiveData<AuthorizationModel> {
        return pref.authorize().asLiveData()
    }

    private fun getNewToken(refreshToken: String, keyword: String, isDisease: Boolean, type: Int) {
        val jsonObject = JSONObject().put("refreshToken", refreshToken)
        val requestBody =
            jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())

        val client = ApiConfig.getApiService().refreshToken(requestBody)
        client.enqueue(object : Callback<TokenResponse> {
            override fun onResponse(
                call: Call<TokenResponse>,
                response: Response<TokenResponse>
            ) {
                Log.d("tokenresponse", response.toString())
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _newToken.value = responseBody.accessToken
                        if (type == 1) {
                            getDetail(_newToken.value ?: "", refreshToken, keyword, isDisease)
                        } else {
                            getDiseaseMedicine(_newToken.value ?: "", refreshToken, keyword)
                        }
                    }
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                    logout()
                }
            }

            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: get new token fail")
            }
        })
    }

    fun getDetail(accessToken: String, refreshToken: String, slug: String, isDisease: Boolean) {
        _isLoading.value = true
        val client = if (isDisease) {
            ApiConfig.getApiService().getDiseaseDetail("Bearer $accessToken", slug)
        } else {
            ApiConfig.getApiService().getMedicineDetail("Bearer $accessToken", slug)
        }

        if (isDisease) {
            getDiseaseMedicine(accessToken, refreshToken, slug)
        }

        client.enqueue(object : Callback<List<DetailItemResponseItem>> {
            override fun onResponse(
                call: Call<List<DetailItemResponseItem>>,
                response: Response<List<DetailItemResponseItem>>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _detailItemResponse.value = response.body()
                        _isLoading.value = false
                    }
                } else {
                    getNewToken(refreshToken, slug, isDisease, 1)
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<DetailItemResponseItem>>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: get disease server fail")
                _isLoading.value = false
            }
        })
    }

    fun getDiseaseMedicine(accessToken: String, refreshToken: String, slug: String) {
        _isLoadingMed.value = true
        val client = ApiConfig.getApiService().getDiseaseMedicine("Bearer $accessToken", slug)
        client.enqueue(object : Callback<List<ListItemResponse>> {
            override fun onResponse(
                call: Call<List<ListItemResponse>>,
                response: Response<List<ListItemResponse>>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _medicineResponse.value = response.body()
                        _isLoadingMed.value = false
                    }
                } else {
                    getNewToken(refreshToken, slug, true, 2)
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ListItemResponse>>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: get disease medicine server fail")
                _isLoadingMed.value = false
            }
        })
    }
}