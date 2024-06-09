package com.wesal.entities.models
import com.google.gson.annotations.SerializedName



data class NotificationsModel(
    @SerializedName("notify_id")
    val notifyId: String,
    @SerializedName("notify_text")
    val notifyText: String,
    @SerializedName("notify_type")
    val notifyType: String,
    @SerializedName("notify_type_id")
    val notifyTypeId: Int,
    @SerializedName("notify_advertiser_id")
    val notifyAdvertiserId: Int,
    @SerializedName("notify_type_title")
    val notifyTypeTitle: String,
    @SerializedName("notify_reading")
    val notifyReading: Boolean,
    @SerializedName("notify_custom_date")
    val notifyCustomDate: String,
    @SerializedName("notify_created_at")
    val notifyCreatedAt: String,
    @SerializedName("notify_updated_at")
    val notifyUpdatedAt: String
)