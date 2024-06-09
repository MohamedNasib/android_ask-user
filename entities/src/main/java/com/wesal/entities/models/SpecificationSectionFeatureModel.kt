package com.wesal.entities.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SpecificationSectionFeatureModel(
    @SerializedName("feature_id")
    val featureId: Int,
    @SerializedName("feature_section")
    val featureSection: FeatureSectionModel,
    @SerializedName("feature_name")
    val featureName: String,
    @SerializedName("feature_type")
    val featureType: String
) : Parcelable