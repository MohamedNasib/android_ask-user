package com.wesal.entities.base

import com.google.gson.annotations.SerializedName

data class PaginationInfoModel(
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