package com.wesal.entities.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AdvertSpecificationModel(
    @SerializedName("specification_id")
    val specificationId: Int,
    @SerializedName("specification_adv_id")
    val specificationAdvId: Int,
    @SerializedName("specification_section_feature")
    val specificationSectionFeature: SpecificationSectionFeatureModel,
    @SerializedName("specification_answer")
    val specificationAnswer: String,
    @SerializedName("specification_answer_id")
    val specificationAnswerId: Int
) : Parcelable