package com.wesal.askhail.features.paymentWebView

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.extentions.setToolbar
import com.wesal.askhail.core.extentions.setWhiteActivity
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.databinding.ActivityInactiveAdvertListBinding
import com.wesal.askhail.databinding.ActivityPaymentWebViewBinding
import com.wesal.askhail.databinding.ActivitySuccessPageBinding
import com.wesal.domain.useCases.UseCaseImpl
import org.json.JSONArray
import java.util.*

class PaymentWebViewActivity : BaseActivity(), MyJavaScriptInterface.OnPaymentResponse {
    lateinit var binding: ActivityPaymentWebViewBinding
    private var paymentUrl: String? = null
    var mj: MyJavaScriptInterface? = null

    override fun layoutResource(): Int = R.layout.activity_payment_web_view
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentWebViewBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setToolbar(getString(R.string.e_pay))
        setWhiteActivity()
        getLanguageAwareContext(this)
         paymentUrl = intent.getStringExtra(ExtraConst.EXTRA_PAYMENT_URL)
        if (paymentUrl == null) {
            onBackPressed()
        }
        initWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        mj = MyJavaScriptInterface(this)
        binding.paymentWebView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        binding.paymentWebView.settings.javaScriptEnabled = true
        binding.paymentWebView.settings.loadsImagesAutomatically = true
        binding.paymentWebView.settings.useWideViewPort = true
        binding.paymentWebView.settings.loadWithOverviewMode = true
        binding.paymentWebView.settings.setSupportZoom(false)
        binding.paymentWebView.addJavascriptInterface(mj!!, "PrintPayment")
        binding.paymentWebView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                Log.e("onPageStarted", "-->$url")
                try {
                } catch (e: Exception) {
                }
            }
            override fun onPageCommitVisible(view: WebView?, url: String?) {
                Log.e("onPageCommitVisible", "-->$url")
                super.onPageCommitVisible(view, url)
                try {
                } catch (e: Exception) {
                }
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.e("onPageFinished", "-->$url")
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    try {
                    } catch (e: Exception) {
                    }
                }
            }
        }
        paymentUrl?.let { binding.paymentWebView.loadUrl(it) }
    }

    private fun getLanguageAwareContext(context: Context): Context? {
        val savedLang: String = UseCaseImpl().getSystemLanguage()
        val country: String
        country = if (savedLang.equals("ar", ignoreCase = true)) {
            "EG"
        } else {
            "US"
        }
        val newLocale = Locale(savedLang, country)
        Locale.setDefault(newLocale)
        val configuration =
            context.resources.configuration
        configuration.setLocale(newLocale)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocales(LocaleList(newLocale))
            context.resources
                .updateConfiguration(configuration, context.resources.displayMetrics)
        }
        return context.createConfigurationContext(configuration)
    }

    override fun onPostPayment(value: String?) {
        value?.let {
            Log.e("PAYMENT", "$it")
//            Log.e("paymentInfo", "paymentInfo " + paymentInfo);
            val paymentData = JSONArray(it)
            if (paymentData.length() == 0) {
                val intent = Intent()
                intent.putExtra("isSuccess", false)
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else {
                val jsonObject = paymentData.getJSONObject(0)
                val success = jsonObject.getBoolean("status")
                if (success) {
                    val orderId = jsonObject.getJSONObject("data").getInt("advertisement_id")
                    val intent = Intent()
                    intent.putExtra("isSuccess", true)
                    intent.putExtra("advertId", orderId)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                } else {
                    val intent = Intent()
                    intent.putExtra("isSuccess", false)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }

            }
        } ?: kotlin.run {
            val intent = Intent()
            intent.putExtra("isSuccess", false)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

    }
}



