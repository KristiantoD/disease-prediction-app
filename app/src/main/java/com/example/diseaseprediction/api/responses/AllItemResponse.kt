package com.example.diseaseprediction.api.responses

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AllItemResponse(

    @field:SerializedName("AllItemResponse")
    val allItemResponse: List<ListItemResponse>
) : Parcelable

@Parcelize
data class ListItemResponse(
    @field:SerializedName("img")
    val img: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("slug")
    val slug: String,

    @field:SerializedName("excerpt")
    val excerpt: String,
) : Parcelable
