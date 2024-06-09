package com.wesal.entities.models
import com.google.gson.annotations.SerializedName



data class AddCommentModel(
    @SerializedName("message")
    val message: String,
    @SerializedName("comment")
    val comment: CommentModel
)

