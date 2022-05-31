package com.example.diseaseprediction.api.responses

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @field:SerializedName("msg")
    val msg: String
)
