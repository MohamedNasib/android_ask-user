package com.wesal.entities.models
import com.google.gson.annotations.SerializedName


data class OrderContentModel(
    @SerializedName("order_details")
    val orderDetails: OrderDetailsModel,
    @SerializedName("comments_count")
    val commentsCount: String,
    @SerializedName("comments_data")
    val commentsData: List<CommentModel>
)


