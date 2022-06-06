package com.example.diseaseprediction.api.responses

import com.google.gson.annotations.SerializedName

data class PredictionResponse(

    @field:SerializedName("result")
    val result: List<ResultItem>
)

data class ResultItem(

    @field:SerializedName("disease")
    val disease: String,

    @field:SerializedName("deskripsi")
    val deskripsi: String,

    @field:SerializedName("probability")
    val probability: Float

)
