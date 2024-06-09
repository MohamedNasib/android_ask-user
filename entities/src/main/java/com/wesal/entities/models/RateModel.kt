package com.wesal.entities.models
import com.google.gson.annotations.SerializedName

data class RateModel(
    @SerializedName("rate_id")
    val rateId: Int,
    @SerializedName("rate_voter_id")
    val rateVoterId: Int,
    @SerializedName("rate_voter_name")
    val rateVoterName: String,
    @SerializedName("rate")
    val rate: Float,
    @SerializedName("rate_custom_date")
    val rateCustomDate: String,
    @SerializedName("rate_created_at")
    val rateCreatedAt: String,
    @SerializedName("rate_updated_at")
    val rateUpdatedAt: String
)