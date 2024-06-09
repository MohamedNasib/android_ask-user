package com.wesal.entities.models
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class BlocksModel(
    @SerializedName("block_id")
    val blockId: Long,
    @SerializedName("block_city")
    val blockCity: BlockCity,
    @SerializedName("block_name")
    val blockName: String,
    @SerializedName("admin_block_name")
    val adminBlockName: String
) : Parcelable

@Parcelize
data class BlockCity(
    @SerializedName("city_id")
    val cityId: Int,
    @SerializedName("city_region")
    val cityRegion: CityRegion,
    @SerializedName("city_name")
    val cityName: String,
    @SerializedName("admin_city_name")
    val adminCityName: String
) : Parcelable

@Parcelize
data class CityRegion(
    @SerializedName("region_id")
    val regionId: Int,
    @SerializedName("region_name")
    val regionName: String
) : Parcelable