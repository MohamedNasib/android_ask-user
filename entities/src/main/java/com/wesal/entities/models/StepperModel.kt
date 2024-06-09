package com.wesal.entities.models
import com.google.gson.annotations.SerializedName



data class StepperModel(
    @SerializedName("advertiser_id")
    val advertiserId: Int,
    @SerializedName("next_level")
    val nextLevel: Int
)