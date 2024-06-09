package com.wesal.entities.models
import com.google.gson.annotations.SerializedName


data class BanksDetailsModel(
    @SerializedName("bank_account_id")
    val bankAccountId: Int,
    @SerializedName("bank_account_name")
    val bankAccountName: String,
    @SerializedName("bank_account_logo")
    val bankAccountLogo: String,
    @SerializedName("bank_account_user_name")
    val bankAccountUserName: String,
    @SerializedName("bank_account_number")
    val bankAccountNumber: String,
    @SerializedName("bank_account_iban")
    val bankAccountIban: String
)