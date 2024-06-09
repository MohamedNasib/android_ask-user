package com.wesal.entities.models

import com.google.gson.annotations.SerializedName

data class PackageDetailsModel(
    @SerializedName("subscription_start_date")
    val subscriptionStartDate: String,
    @SerializedName("subscription_custom_start_date")
    val subscriptionCustomStartDate: String,
    @SerializedName("subscription_end_date")
    val subscriptionEndDate: String,
    @SerializedName("subscription_custom_end_date")
    val subscriptionCustomEndDate: String,
    @SerializedName("subscription_used_advertisements")
    val subscriptionUsedAdvertisements: Int,
    @SerializedName("subscription_rest_days")
    val subscriptionRestDays: Int,
    @SerializedName("subscription_can_renew_package_status")
    val subscriptionCanRenewPackageStatus: Boolean
)