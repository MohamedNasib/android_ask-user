package com.wesal.entities.models
import com.google.gson.annotations.SerializedName



data class HomeModel(
        @SerializedName("notifications_count")
    val notificationsCount: String,
        @SerializedName("special_advertisements")
    val specialAdvertisements: List<SpecialAdvertisementModel>,
        @SerializedName("real_estate")
    val realEstate: List<SectionModel>,
        @SerializedName("business")
    val business: List<SectionModel>
)
