package com.wesal.entities.models
import com.google.gson.annotations.SerializedName


data class HighlightInfoModel(
    @SerializedName("special_image")
    val specialImage: String,
    @SerializedName("special_description")
    val specialDescription: String,
    @SerializedName("special_advantages")
    val specialAdvantages: List<String>
)