package com.wesal.entities.models
import com.google.gson.annotations.SerializedName



data class AskHailContentModel(
    @SerializedName("question_details")
    val questionDetails: QuestionDetailsModel,
    @SerializedName("comments_count")
    val commentsCount: String,
    @SerializedName("comments_data")
    val commentsData: List<CommentModel>
)
