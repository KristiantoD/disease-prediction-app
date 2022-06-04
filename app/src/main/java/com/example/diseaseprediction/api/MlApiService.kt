package com.example.diseaseprediction.api

import com.example.diseaseprediction.api.responses.ResultItem
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface MlApiService {
    @POST("/predict")
    fun predict(
        @Body requestBody: RequestBody
    ): Call<List<ResultItem>>
}