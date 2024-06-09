package com.wesal.entities.models

import com.google.gson.annotations.SerializedName


data class FeatureDataModel(
    @SerializedName("data_id")
    val dataId: Int,
    @SerializedName("data_feature_id")
    val dataFeatureId: Int,
    @SerializedName("data_title")
    val dataTitle: String
)