package com.wesal.entities.models
import com.google.gson.annotations.SerializedName


data class UserModel(
    @SerializedName("advertiser_id")
    val advertiserId: Int,
    @SerializedName("advertiser_type")
    val advertiserType: String,
    @SerializedName("advertiser_name")
    val advertiserName: String,
    @SerializedName("advertiser_email")
    val advertiserEmail: String,
    @SerializedName("advertiser_mobile")
    val advertiserMobile: String,
    @SerializedName("advertiser_api_token")
    val advertiserApiToken: String,
    @SerializedName("advertiser_firebase_token")
    val advertiserFirebaseToken: String,
    @SerializedName("advertiser_package_id")
    val advertiserPackageId: String
)