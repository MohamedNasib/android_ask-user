package com.wesal.entities.models
import com.google.gson.annotations.SerializedName


data class SettingsModel(
    @SerializedName("advertiser_id")
    val advertiserId: Int,
    @SerializedName("advertiser_all_notifications_status")
    val advertiserAllNotificationsStatus: String,
    @SerializedName("advertiser_new_comments_status")
    val advertiserNewCommentsStatus: String,
    @SerializedName("advertiser_chat_status")
    val advertiserChatStatus: String,
    @SerializedName("advertiser_question_replies_status")
    val advertiserQuestionRepliesStatus: String,
    @SerializedName("advertiser_favorite_status")
    val advertiserFavoriteStatus: String,
    @SerializedName("advertiser_language_used")
    val advertiserLanguageUsed: String
)