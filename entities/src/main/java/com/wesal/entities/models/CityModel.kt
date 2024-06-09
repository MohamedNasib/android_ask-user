package com.wesal.entities.models

import com.google.gson.annotations.SerializedName


data class CityModel(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("zip_code")
    val zipCode: String
)