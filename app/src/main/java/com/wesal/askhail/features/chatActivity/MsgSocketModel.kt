package com.wesal.askhail.features.chatActivity
import com.google.gson.annotations.SerializedName
import com.wesal.entities.models.MessagesDataModel

data class MsgSocketModel (
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("code")
    val code: String,
    @SerializedName("data")
    val `data`: MessagesDataModel?
)

