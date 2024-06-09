package com.wesal.entities.models
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class PackageModel(
    @SerializedName("package_id")
    val packageId: Int,
    @SerializedName("package_name")
    val packageName: String,
    @SerializedName("package_description")
    val packageDescription: String,
    @SerializedName("package_special_with")
    val packageSpecialWith: String?,
    @SerializedName("package_rate")
    val packageRate: String,
    @SerializedName("package_duration_in_days")
    val packageDurationInDays: String,
    @SerializedName("package_price")
    val packagePrice: String,
    @SerializedName("package_advertisements_count")
    val packageAdvertisementsCount: String?
) : Parcelable