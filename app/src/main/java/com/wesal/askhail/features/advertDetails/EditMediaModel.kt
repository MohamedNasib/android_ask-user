package com.wesal.askhail.features.advertDetails

import android.os.Parcelable
import com.wesal.entities.models.AdvMediaModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class EditMediaModel(
    val promoImage:String?,
    val media :List<AdvMediaModel>
) : Parcelable