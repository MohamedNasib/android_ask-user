package com.wesal.askhail.core.presentationEnums

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class SuccessRoute : Parcelable {
    REGISTER,CREATE_ADVERT,CREATE_ORDER,CREATE_ASK,APPLY_JOB,BANK_TRANSFER_HIGHLIGHT,
    NORMAL_TRANSFER
}