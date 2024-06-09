package com.wesal.entities.models
import com.google.gson.annotations.SerializedName


data class ServicesModel(
    @SerializedName("service_id")
    val serviceId: Int,
    @SerializedName("service_type")
    val serviceType: String,
    @SerializedName("service_title")
    val serviceTitle: String,
    @SerializedName("service_file")
    val serviceFile: String,
    @SerializedName("service_custom_added_date")
    val serviceCustomAddedDate: String,
    @SerializedName("service_added_date")
    val serviceAddedDate: String
)