package com.wesal.entities.base

import com.google.gson.annotations.SerializedName
import com.wesal.entities.models.AdverterInfoModel


data class PaginationModel<T>(
    @SerializedName("data")
    val data: List<T>,
    @SerializedName("pagination")
    val pagination: PaginationInfoModel,
    @SerializedName("if_have_blocked_advertisements")
    val hasInactiveAdverts:Boolean?  ,
    @SerializedName("advertiser_info")
    val advertiserInfo: AdverterInfoModel?
)