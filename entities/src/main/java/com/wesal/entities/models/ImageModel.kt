package com.wesal.entities.models

import com.google.gson.annotations.SerializedName


data class ImageModel(
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String
)