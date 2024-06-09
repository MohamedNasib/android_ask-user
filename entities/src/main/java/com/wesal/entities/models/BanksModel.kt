package com.wesal.entities.models
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class BanksModel(
    @SerializedName("bank_account_id")
    val bankAccountId: Int,
    @SerializedName("bank_account_name")
    val bankAccountName: String,
    @SerializedName("bank_account_logo")
    val bankAccountLogo: String
) : Parcelable