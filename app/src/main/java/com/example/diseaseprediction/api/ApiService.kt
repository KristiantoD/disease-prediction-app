package com.example.diseaseprediction.api

import com.example.diseaseprediction.api.responses.*
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

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

    @GET("/diseases/{slug}")
    fun getDiseaseDetail(
        @Header("Authorization") token: String,
        @Path("slug") slug: String
    ): Call<List<DetailItemResponseItem>>

    @GET("/diseases-drugs/diseases/{slug}")
    fun getDiseaseMedicine(
        @Header("Authorization") token: String,
        @Path("slug") slug: String
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

    @GET("/drugs/{slug}")
    fun getMedicineDetail(
        @Header("Authorization") token: String,
        @Path("slug") slug: String
    ): Call<List<DetailItemResponseItem>>

    @POST("/token-app")
    fun refreshToken(
        @Body requestBody: RequestBody
    ): Call<TokenResponse>
}