package com.wesal.entities.models
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SidesModel(
    @SerializedName("side_id")
    val sideId: Int,
    @SerializedName("side_name")
    val sideName: String
) : Parcelable