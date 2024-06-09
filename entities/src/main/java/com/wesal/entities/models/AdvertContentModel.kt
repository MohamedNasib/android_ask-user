package com.wesal.entities.models

import com.google.gson.annotations.SerializedName

data class AdvertContentModel(
    @SerializedName("advertisement_details")
    val advertisementDetails: AdvertDetailsModel,
    @SerializedName("rates_count")
    val ratesCount: String,
    @SerializedName("rates_average")
    val ratesAverage: String,
    @SerializedName("rates_data")
    val ratesData: List<RateModel>,
    @SerializedName("comments_count")
    val commentsCount: String?,
    @SerializedName("comments_data")
    val commentsData: List<CommentModel>?

)