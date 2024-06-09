package com.wesal.askhail.features.advertDetails

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EditSocialModel(
    val twitter:String?,
    val instagram:String?,
    val snapchat:String?,
    val facebook:String?,
    val website:String?
    ) : Parcelable