package com.example.diseaseprediction.api.responses

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailItemResponse(

    @field:SerializedName("DetailItemResponse")
    val detailItemResponse: List<DetailItemResponseItem?>? = null
) : Parcelable

@Parcelize
data class DetailItemResponseItem(

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("img")
    val img: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("other_name")
    val otherName: String? = null,

    @field:SerializedName("excerpt")
    val excerpt: String? = null,

    @field:SerializedName("slug")
    val slug: String? = null,

    @field:SerializedName("updatedAt")
    val updatedAt: String? = null
) : Parcelable
