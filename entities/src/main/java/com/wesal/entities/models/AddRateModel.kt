package com.wesal.entities.models
import com.google.gson.annotations.SerializedName
data class AddRateModel(
    @SerializedName("message")
    val message: String,
    @SerializedName("rate")
    val rate: RateModel
)
