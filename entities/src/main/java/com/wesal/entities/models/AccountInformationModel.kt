package com.wesal.entities.models
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize



@Parcelize
data class AccountInformationModel(
    @SerializedName("advertiser_id")
    val advertiserId: Int,
    @SerializedName("advertiser_name")
    val advertiserName: String,
    @SerializedName("advertiser_email")
    val advertiserEmail: String,
    @SerializedName("advertiser_mobile")
    val advertiserMobile: String

) : Parcelable