package com.wesal.entities.models

import com.google.gson.annotations.SerializedName


data class BannerModel(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("image")
    val image: String
)