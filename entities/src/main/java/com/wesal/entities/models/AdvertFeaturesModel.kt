package com.wesal.entities.models
import com.google.gson.annotations.SerializedName



data class AdvertFeaturesModel(
    @SerializedName("feature_id")
    val featureId: Int,
    @SerializedName("feature_name")
    val featureName: String,
    @SerializedName("feature_type")
    val featureType: String,
    @SerializedName("feature_data")
    val featureData: List<FeatureDataModel>,
    var selectedText:String = "",
    var selectedValue:String = ""

)

