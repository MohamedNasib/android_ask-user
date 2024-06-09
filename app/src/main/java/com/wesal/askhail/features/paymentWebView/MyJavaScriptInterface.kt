package com.wesal.askhail.features.paymentWebView

import android.webkit.JavascriptInterface


class MyJavaScriptInterface(//    static String PrintPayment;
    private val callBack: OnPaymentResponse
) {
    @JavascriptInterface
    fun postMessage(value: String?) {
//        PrintPayment = value;+++
        callBack.onPostPayment(value)
        //        Log.e("PAYMENT"," "+value);
    }

    interface OnPaymentResponse {
        fun onPostPayment(value: String?)
    }

}
