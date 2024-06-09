package com.wesal.entities.models

import com.google.gson.annotations.SerializedName

data class MyPackageModel(
    @SerializedName("package_id")
    val packageId: Int,
    @SerializedName("package_name")
    val packageName: String,
    @SerializedName("package_description")
    val packageDescription: String,
    @SerializedName("package_rate")
    val packageRate: String,
    @SerializedName("package_duration_in_days")
    val packageDurationInDays: String,
    @SerializedName("package_price")
    val packagePrice: String,
    @SerializedName("package_advertisements_count")
    val packageAdvertisementsCount: String,
    @SerializedName("package_details")
    val packageDetails: PackageDetailsModel?,
    @SerializedName("package_if_subscription_type_is_later")
    val packageIfSubscriptionTypeIsLater: Boolean
)