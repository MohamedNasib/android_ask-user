package com.wesal.entities.models

import com.google.gson.annotations.SerializedName

data class SpecialAdvertisementModel(
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
        val advViews: String
)
