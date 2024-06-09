package com.wesal.entities.models
import com.google.gson.annotations.SerializedName




data class MessagesModel(
    @SerializedName("chat_id")
    val chatId: Int,
    @SerializedName("chat_title")
    val chatTitle: String,
    @SerializedName("chat_image")
    val chatImage: String,
    @SerializedName("chat_last_message")
    val chatLastMessage: String,
    @SerializedName("chat_last_message_date")
    val chatLastMessageDate: String,
    @SerializedName("chat_unread_messages_count")
    var chatUnreadMessagesCount: Int,
    @SerializedName("chat_created_at")
    val chatCreatedAt: String,
    @SerializedName("chat_updated_at")
    val chatUpdatedAt: String
)
