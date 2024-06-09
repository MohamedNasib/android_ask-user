package com.wesal.entities.models

import com.google.gson.annotations.SerializedName


data class AdvertModel(
    @SerializedName("adv_id")
    val advId: Int,
    @SerializedName("adv_advertiser_id")
    val advAdvertiserId: Int,
    @SerializedName("adv_special_status")
    val advSpecialStatus: String,
    @SerializedName("adv_promotional_image")
    val advPromotionalImage: String,
    @SerializedName("adv_title")
    val advTitle: String,
    @SerializedName("adv_is_favorite")
    var advIsFavorite: Boolean,
    @SerializedName("adv_price")
    val advPrice: String,
    @SerializedName("adv_distance")
    val advDistance: String,
    @SerializedName("adv_views")
    val advViews: String,
    @SerializedName("adv_available_status")
    val advAvailableStatus: String?,
    @SerializedName("adv_available_custom_status")
    val advAvailableCustomStatus: String?,
    @SerializedName("adv_total_rate")
    val advTotalRate: String?,
    @SerializedName("adv_lat")
    val advLat: String,
    @SerializedName("adv_lng")
    val advLng: String,
    @SerializedName("adv_media")
    val advMedia: List<AdvMediaModel>
)

