package com.wesal.entities.models
import com.google.gson.annotations.SerializedName





data class OrdersModel(
    @SerializedName("order_id")
    val orderId: Int,
    @SerializedName("order_advertiser_id")
    val orderAdvertiserId: Int,
    @SerializedName("order_advertiser_name")
    val orderAdvertiserName: String,
    @SerializedName("order_title")
    val orderTitle: String,
    @SerializedName("order_price")
    val orderPrice: String,
    @SerializedName("order_custom_date")
    val orderCustomDate: String,
    @SerializedName("order_date_status")
    val orderDateStatus: String,
    @SerializedName("order_last_update")
    val orderLastUpdate: String,
    @SerializedName("order_created_at")
    val orderCreatedAt: String,
    @SerializedName("order_updated_at")
    val orderUpdatedAt: String
)

