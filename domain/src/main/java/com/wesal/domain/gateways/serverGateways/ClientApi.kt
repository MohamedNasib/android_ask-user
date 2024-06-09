package com.wesal.domain.gateways.serverGateways

import android.util.Log
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import com.wesal.domain.gateways.preferencesGateways.PrefManger
import com.wesal.domain.gateways.preferencesGateways.preferencesGateway
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import okhttp3.HttpUrl


val myRetrofit by lazy { ClientApi.retrofit }

object ClientApi {
    private val pref: PrefManger = preferencesGateway
    val retrofit: Retrofit by lazy {

        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private val baseUrl by lazy {
            //https://askhail.com/api/v1
        HttpUrl.Builder()
            .scheme("https")
            //.host("askhail.com")
            .host("askhail.com.sa")
            .addPathSegment("api")
            .addPathSegment("v1")
            .addPathSegment("")
            .build()
    }
    private val baseUrlForDevelopment by lazy {
        HttpUrl.Builder()
            .scheme("https")
            .host("askhail.com")
            .addPathSegment("api")
            .addPathSegment("v1")
            .addPathSegment("")
            .build()
    }
    private val client by lazy {

        Log.e("TRY", "-->" + pref.loadCountryId().toString())
        Log.e("TRY", "-->" + pref.getAuthorization())
        Log.e("TRY", "-->" + pref.getFireBaseToken())
        OkHttpClient.Builder()
            .addInterceptor(
                LoggingInterceptor.Builder()
                    .setLevel(Level.BASIC)
                    .log(Log.VERBOSE)
                    .build()
            )
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .connectTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val request = chain.request()
                    .newBuilder()
                    .addHeader("Accept-Language", pref.getSystemLanguage())
                    .addHeader("Authorization", pref.getAuthorization())
                    .addHeader("countryId", pref.loadCountryId().toString())
                    .addHeader("lat", pref.getUserLocation().latitude.toString())
                    .addHeader("lng", pref.getUserLocation().longitude.toString())
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/json")
                    .build()
                chain.proceed(request)
            }
            .build()
    }

}