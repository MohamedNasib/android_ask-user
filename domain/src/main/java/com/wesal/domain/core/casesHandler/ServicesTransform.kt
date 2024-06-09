package com.wesal.domain.core.casesHandler

import android.util.Log
import com.wesal.entities.base.Calling
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import timber.log.Timber
import java.io.IOException

object ServicesTransform {
    fun <T : Any> transform(api: Call<Calling<T>>): Results<T?> {
        return try {
            handleCodesCases(api)
        } catch (exception: Exception) {
            Timber.e("-->${exception.message
            }")
            if (exception is IOException)
                Results.Error(Results.FailureReason.OFFLINE)
            else
                Results.Error(Results.FailureReason.UNKNOWN_REASON)
        }
    }

    private fun <T : Any> handleCodesCases(api: Call<Calling<T>>): Results<T?> {
        val response = api.execute()
        return if (response.isSuccessful) {
            Results.Success(response.body()?.data, getServerMessageFromResponse(response.body()))
        } else {
            when (response.code()) {
                401 -> Results.Error(Results.FailureReason.UNAUTHORIZED)
                in 400..500 -> Results.Error(
                    Results.FailureReason.USER_SIDE,
                    getServerErrorFromResponse(response.errorBody())
                )
                in 500..600 -> Results.Error(Results.FailureReason.SERVER_SIDE)
                else -> Results.Error(Results.FailureReason.UNKNOWN_REASON)
            }
        }
//            return if (exception is IOException){
//                Results.Error(Results.FailureReason.OFFLINE)
//            }else{
//                Results.Error(Results.FailureReason.UNKNOWN_REASON)
//
//            }
    }

    private fun <T> getServerMessageFromResponse(body: Calling<T>?): String? {
        body?.let {
            it.message?.let { y ->
                if (y.isNotEmpty()) {
                    return y
                }
            }
        }
        return null
    }

    private fun getServerErrorFromResponse(errorBody: ResponseBody?): String? {
        var value = "Un Handle Error"
        errorBody?.let {
            try {
                val json = JSONObject(errorBody.string())
                value = json.getString("message")
            } catch (e: Exception) {
                Timber.e("UNHANDLE ERROR ${e.message}")
            }
        }
        return value

    }
}