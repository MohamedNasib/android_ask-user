package com.wesal.entities.models

import com.google.gson.annotations.SerializedName

data class QuestionDetailsModel(
    @SerializedName("question_id")
    val questionId: Int,
    @SerializedName("question_image")
    val questionImage: String,
    @SerializedName("question_show_name_status")
    val questionShowNameStatus: String,
    @SerializedName("question_advertiser_id")
    val questionAdvertiserId: Int,
    @SerializedName("question_advertiser_name")
    val questionAdvertiserName: String,
    @SerializedName("question_title")
    val questionTitle: String,
    @SerializedName("question_custom_published_date")
    val questionCustomPublishedDate: String,
    @SerializedName("question_custom_last_update_date")
    val questionCustomLastUpdateDate: String,
    @SerializedName("question_description")
    val questionDescription: String,
    @SerializedName("question_last_update")
    val questionLastUpdate: String,
    @SerializedName("question_created_at")
    val questionCreatedAt: String,
    @SerializedName("question_updated_at")
    val questionUpdatedAt: String
)
