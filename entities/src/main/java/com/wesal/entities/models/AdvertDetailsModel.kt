package com.wesal.entities.models

import com.google.gson.annotations.SerializedName

data class AdvertDetailsModel(
    @SerializedName("adv_id")
    val advId: Int,
    @SerializedName("adv_advertiser_id")
    val advAdvertiserId: Int,
    @SerializedName("adv_type")
    val advType: String,
    @SerializedName("adv_is_favorite")
    var advIsFavorite: Boolean,
    @SerializedName("adv_special_status")
    val advSpecialStatus: String,
    @SerializedName("adv_media")
    val advMedia: List<AdvMediaModel>,
    @SerializedName("adv_promotional_image")
    val advPromotionalImage: String,
    @SerializedName("adv_title")
    val advTitle: String,
    @SerializedName("adv_price")
    val advPrice: String,
    @SerializedName("adv_distance")
    val advDistance: String,
    @SerializedName("adv_views")
    val advViews: String,
    @SerializedName("adv_total_rate")
    val advTotalRate: String,
    @SerializedName("adv_custom_published_date")
    val advCustomPublishedDate: String,
    @SerializedName("adv_custom_last_update_date")
    val advCustomLastUpdateDate: String,
    @SerializedName("adv_last_update")
    val advLastUpdate: String,
    @SerializedName("adv_description")
    val advDescription: String,
    @SerializedName("adv_specifications")
    val advSpecifications: List<AdvertSpecificationModel>,
    @SerializedName("adv_location")
    val advLocation: String,
    @SerializedName("adv_latitude")
    val advLatitude: String,
    @SerializedName("adv_longitude")
    val advLongitude: String,
    @SerializedName("adv_advertiser_name")
    val advAdvertiserName: String,
    @SerializedName("adv_advertiser_advs_count")
    val advAdvertiserAdvsCount: String,
    @SerializedName("adv_if_any_contact_available")
    val advIfAnyContactAvailable: Boolean,
    @SerializedName("adv_call_number_status")
    val advCallNumberStatus: String,
    @SerializedName("adv_call_number")
    val advCallNumber: String?,
    @SerializedName("adv_whatsapp_number_status")
    val advWhatsappNumberStatus: String,
    @SerializedName("adv_whatsapp_number")
    val advWhatsappNumber: String?,
    @SerializedName("adv_twitter")
    val advTwitter: String?,
    @SerializedName("adv_instagram")
    val advInstagram: String?,
    @SerializedName("adv_snapchat")
    val advSnapchat: String?,
    @SerializedName("adv_facebook")
    val advFacebook: String?,
    @SerializedName("adv_website")
    val advWebsite: String?,
    @SerializedName("adv_created_at")
    val advCreatedAt: String,
    @SerializedName("adv_updated_at")
    val advUpdatedAt: String ,
    @SerializedName("adv_main_section")
    val mainSection: SectionModel,
    @SerializedName("adv_sub_section")
    val subSection: SectionModel,

    @SerializedName("adv_block")
    val advBlock: BlocksModel,


    @SerializedName("adv_side")
    val advSide: SidesModel,



    @SerializedName("isWaiting")
    val isWaiting: Boolean
)
