package com.wesal.entities.models
import com.google.gson.annotations.SerializedName


data class AdverterInfoModel(
    @SerializedName("advertiser_id")
    val advertiserId: Int,
    @SerializedName("advertiser_mobile")
    val advertiserMobile: String,
    @SerializedName("advertiser_whatsapp")
    val advertiserWhatsapp: String
)
