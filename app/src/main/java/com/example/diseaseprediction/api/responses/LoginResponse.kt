package com.example.diseaseprediction.api.responses

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginResponse(
    @field:SerializedName("accessToken")
    val accessToken: String,

    @field:SerializedName("refreshToken")
    val refreshToken: String,

    @field:SerializedName("user")
    val user: List<UserItem?>? = null
) : Parcelable

@Parcelize
data class UserItem(
    @field:SerializedName("img")
    val img: String? = null,

    @field:SerializedName("address")
    val address: String? = null,

    @field:SerializedName("birthdate")
    val birthdate: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("phonenum")
    val phonenum: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("username")
    val username: String? = null
) : Parcelable
