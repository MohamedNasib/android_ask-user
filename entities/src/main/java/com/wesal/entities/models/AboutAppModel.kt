package com.wesal.entities.models
import com.google.gson.annotations.SerializedName



data class AboutAppModel(
    @SerializedName("app_name")
    val appName: String,
    @SerializedName("app_logo")
    val appLogo: String,
    @SerializedName("app_description")
    val appDescription: String,
    @SerializedName("app_marquee")
    val appMarquee: String,
    @SerializedName("app_mobile")
    val appMobile: String,
    @SerializedName("app_twitter")
    val appTwitter: String,
    @SerializedName("app_snapchat")
    val appSnapchat: String,
    @SerializedName("app_instagram")
    val appInstagram: String,
    @SerializedName("app_whatsapp")
    val appWhatsapp: String,
    @SerializedName("app_tiktok")
    val appTikTok: String,
    @SerializedName("app_e_payment_activation")
    val appEPaymentActivation: Boolean
)