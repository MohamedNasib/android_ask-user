package com.wesal.entities.models
import com.google.gson.annotations.SerializedName


data class ChattingContentModel(
    @SerializedName("chat_details")
    val chatDetails: ChatDetailsModel,
    @SerializedName("messages_data")
    val messagesData: List<MessagesDataModel>,
    @SerializedName("messages_pagination")
    val messagesPagination: MessagesPaginationModel
)

data class ChatDetailsModel(
    @SerializedName("chat_id")
    val chatId: Int,
    @SerializedName("chat_type")
    val chatType: String,
    @SerializedName("chat_adv_order_id")
    val chatAdvOrderId: Int,
    @SerializedName("chat_other_name")
    val chatOtherName: String,
    @SerializedName("chat_title")
    val chatTitle: String,
    @SerializedName("chat_last_message_date")
    val chatLastMessageDate: String,
    @SerializedName("chat_created_at")
    val chatCreatedAt: String,
    @SerializedName("chat_updated_at")
    val chatUpdatedAt: String
)

data class MessagesDataModel(
    @SerializedName("message_id")
    val messageId: Int,
    @SerializedName("message_text")
    val messageText: String,
    @SerializedName("message_custom_date")
    val messageCustomDate: String,
    @SerializedName("message_sender_id")
    val messageSenderId: Int,
    @SerializedName("message_if_mine")
    val messageIfMine: Boolean,
    @SerializedName("message_created_at")
    val messageCreatedAt: String,
    @SerializedName("message_updated_at")
    val messageUpdatedAt: String
)

data class MessagesPaginationModel(
    @SerializedName("current_page")
    val currentPage: Int,
    @SerializedName("last_page")
    val lastPage: Int,
    @SerializedName("per_page")
    val perPage: Int,
    @SerializedName("total")
    val total: Int,
    @SerializedName("has_more_pages")
    val hasMorePages: Boolean
)