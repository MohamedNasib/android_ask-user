package com.wesal.askhail.features.advertDetails

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EditContactModel(
    val phone:String?,
    val whats:String?,
    val isPhone:Boolean,
    val isWhats:Boolean
) : Parcelable
