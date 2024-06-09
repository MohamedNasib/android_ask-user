package com.wesal.askhail.features.advertDetails

import android.os.Parcelable
import com.wesal.entities.models.AdvertSpecificationModel
import com.wesal.entities.models.BlocksModel
import com.wesal.entities.models.SidesModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class EditSpecificationModel (
    val title:String,
    val description:String,
    val location:String,
    val lat:String,
    val lng:String,
    val price:String,
    val side:SidesModel?,
    val block:BlocksModel?,
    val features:List<AdvertSpecificationModel>
) : Parcelable
