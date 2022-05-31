package com.example.diseaseprediction.api

import com.example.diseaseprediction.api.responses.ListItemResponse
import com.example.diseaseprediction.api.responses.LoginResponse
import com.example.diseaseprediction.api.responses.RegisterResponse
import com.example.diseaseprediction.api.responses.TokenResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("/register")
    fun register(
        @Body requestBody: RequestBody
    ): Call<RegisterResponse>

    @POST("/login-app")
    fun login(
        @Body requestBody: RequestBody
    ): Call<LoginResponse>

    @GET("/diseases")
    fun getAllDiseases(
        @Header("Authorization") token: String
    ): Call<List<ListItemResponse>>

    @POST("/diseases")
    fun searchDiseases(
        @Header("Authorization") token: String,
        @Body requestBody: RequestBody
    ): Call<List<ListItemResponse>>

    @GET("/drugs")
    fun getAllMedicine(
        @Header("Authorization") token: String
    ): Call<List<ListItemResponse>>

    @POST("/drugs")
    fun searchMedicine(
        @Header("Authorization") token: String,
        @Body requestBody: RequestBody
    ): Call<List<ListItemResponse>>

    @POST("/token-app")
    fun refreshToken(
        @Body requestBody: RequestBody
    ): Call<TokenResponse>
}