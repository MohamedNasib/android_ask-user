package com.wesal.entities.models

import com.google.gson.annotations.SerializedName

data class CommentModel(
        @SerializedName("comment_id")
        val commentId: Int,
        @SerializedName("comment_voter_id")
        val commentVoterId: Int,
        @SerializedName("comment_voter_name")
        val commentVoterName: String,
        @SerializedName("comment_text")
        val commentText: String,
        @SerializedName("comment_text_custom_date")
        val commentTextCustomDate: String,
        @SerializedName("comment_if_advertiser_reply_yet")
        val commentIfAdvertiserReplyYet: Boolean,
        @SerializedName("comment_created_at")
        val commentCreatedAt: String,
        @SerializedName("comment_updated_at")
        val commentUpdatedAt: String ,
        @SerializedName("comment_advertiser_reply")
        val replay: String?,
        @SerializedName("comment_advertiser_reply_custom_date")
        val replayDate: String?
)