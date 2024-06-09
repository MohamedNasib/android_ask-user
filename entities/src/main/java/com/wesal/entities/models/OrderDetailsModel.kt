package com.wesal.entities.models

import com.google.gson.annotations.SerializedName

data class OrderDetailsModel(
    @SerializedName("order_id")
    val orderId: Int,
    @SerializedName("order_title")
    val orderTitle: String,
    @SerializedName("order_price")
    val orderPrice: String,
    @SerializedName("order_custom_published_date")
    val orderCustomPublishedDate: String,
    @SerializedName("order_custom_last_update_date")
    val orderCustomLastUpdateDate: String,
    @SerializedName("order_description")
    val orderDescription: String,
    @SerializedName("order_specifications")
    val orderSpecifications: List<AdvertSpecificationModel>,
//    @SerializedName("order_block")
//    val orderBlock: OrderBlockModel,
    @SerializedName("order_advertiser_id")
    val orderAdvertiserId: Int,
    @SerializedName("order_advertiser_name")
    val orderAdvertiserName: String,
    @SerializedName("order_advertiser_advs_count")
    val orderAdvertiserAdvsCount: String,
    @SerializedName("order_if_any_contact_available")
    val orderIfAnyContactAvailable: Boolean,
    @SerializedName("order_call_number_status")
    val orderCallNumberStatus: String,
    @SerializedName("order_call_number")
    val orderCallNumber: String,
    @SerializedName("order_whatsapp_number")
    val orderWhatsappNumber: String,
    @SerializedName("order_whatsapp_number_status")
    val orderWhatsappNumberStatus: String,
    @SerializedName("order_last_update")
    val orderLastUpdate: String,
    @SerializedName("order_created_at")
    val orderCreatedAt: String,
    @SerializedName("order_updated_at")
    val orderUpdatedAt: String,
    @SerializedName("order_main_section")
    val mainSection: SectionModel,
    @SerializedName("order_sub_section")
    val subSection: SectionModel,
    @SerializedName("order_block")
    val orderBlock: BlocksModel,
    @SerializedName("order_side")
    val orderSide: SidesModel

)
