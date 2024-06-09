package com.wesal.entities.models
import com.google.gson.annotations.SerializedName


data class OrderStepperModel(
    @SerializedName("order_id")
    val orderId: Int,
    @SerializedName("next_level")
    val nextLevel: Int
)