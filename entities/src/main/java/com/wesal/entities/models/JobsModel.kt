package com.wesal.entities.models
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class JobsModel(
    @SerializedName("job_id")
    val jobId: Int,
    @SerializedName("job_title")
    val jobTitle: String,
    @SerializedName("job_description")
    val jobDescription: String
) : Parcelable
