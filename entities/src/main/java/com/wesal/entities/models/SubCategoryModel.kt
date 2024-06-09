package com.wesal.entities.models
import com.google.gson.annotations.SerializedName


data class SubCategoryModel(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("desc")
    val desc: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("main_image")
    val mainImage: String,
    @SerializedName("images")
    val images: List<ImageModel>
)
