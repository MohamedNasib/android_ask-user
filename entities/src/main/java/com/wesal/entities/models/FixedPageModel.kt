package com.wesal.entities.models
import com.google.gson.annotations.SerializedName


data class FixedPageModel(
    @SerializedName("fixed_page_id")
    val fixedPageId: Int,
    @SerializedName("fixed_page_slug")
    val fixedPageSlug: String,
    @SerializedName("fixed_page_title")
    val fixedPageTitle: String,
    @SerializedName("fixed_page_body")
    val fixedPageBody: String
)