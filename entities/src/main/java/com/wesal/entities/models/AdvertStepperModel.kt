package com.wesal.entities.models
import com.google.gson.annotations.SerializedName


data class AdvertStepperModel(
    @SerializedName("advertisement_id")
    val advertisementId: Int,
    @SerializedName("next_level")
    val nextLevel: Int
)