package com.wesal.entities.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName

import kotlinx.parcelize.Parcelize

@Parcelize
data class AdvMediaModel(
    @SerializedName("media_id")
    val mediaId: Int,
    @SerializedName("media_adv_id")
    val mediaAdvId: Int,
    @SerializedName("media_video")
    val mediaVideo: String?,
    @SerializedName("media_image")
    val mediaImage: String ,
    @SerializedName("video_duration")
    val videoDuration: Int
) : Parcelable