package com.wesal.askhail.core.extentions

import android.R.attr.path
import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.wesal.askhail.R
import com.wesal.askhail.core.presentationEnums.ViewEnums
import com.wesal.askhail.features.splash.SplashActivity
import com.wesal.domain.core.casesHandler.Results
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.base.PaginationModel
import java.net.URLConnection


fun Activity.hideStatusBar() {
    if (Build.VERSION.SDK_INT >= 21) {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
    }
}

fun Activity.isUriImage(uri: String?): Boolean {
    val mimeType: String? = URLConnection.guessContentTypeFromName(uri)
    return mimeType != null && mimeType.startsWith("image")
}

fun Activity.hideStatusBarWithWhite() {
    if (Build.VERSION.SDK_INT >= 21) {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
    }
}

fun Activity.setWhiteActivity() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor =
            ContextCompat.getColor(this, R.color.colorWhite);// set status background white

    };

}

@SuppressLint("RestrictedApi")
fun Activity.setToolbar(title: String? = "") {
    val ivBack = findViewById<ImageView>(R.id.ivBack)
    val tvToolBarTitle = findViewById<TextView>(R.id.tvToolBarTitle)
    ivBack?.setOnClickListener { onBackPressed() }
    tvToolBarTitle?.let {
        it.text = title
    }
}

fun Activity.controlViews(
    status: ViewEnums = ViewEnums.VIEW_LOADING,
    defaultEmptyIcon: Int = R.drawable.ic_status_empty_default,
    defaultEmptyString: Int = R.string.empty_default,
    isShowCreateIcon: Boolean = false
) {
    val viewContainer = this.findViewById<View>(R.id.viewContainer)
    val viewLoading = this.findViewById<View>(R.id.viewLoading)
    val viewStatus = this.findViewById<View>(R.id.viewStatus)
    when (status) {
        ViewEnums.VIEW_LOADING -> {
            viewContainer?.let { view -> view.visibility = View.GONE }
            viewLoading?.let { view -> view.visibility = View.VISIBLE }
            viewStatus?.let { view -> view.visibility = View.GONE }
        }
        ViewEnums.VIEW_SHOWING -> {
            viewContainer?.let { view -> view.visibility = View.VISIBLE }
            viewLoading?.let { view -> view.visibility = View.GONE }
            viewStatus?.let { view -> view.visibility = View.GONE }
        }
        else -> {
            viewContainer?.let { view -> view.visibility = View.GONE }
            viewLoading?.let { view -> view.visibility = View.GONE }
            viewStatus?.let { view -> view.visibility = View.VISIBLE }
            val ivStatusImage = this.findViewById<ImageView>(R.id.ivStatusImage)
            val tvStatusMainText = this.findViewById<TextView>(R.id.tvStatusMainText)
            val fabCreatingStatus =
                this.findViewById<FloatingActionButton>(R.id.fabCreatingStatus)
            fabCreatingStatus?.let {
                if (isShowCreateIcon) {
                    it.visible()
                } else {
                    it.gone()
                }
            }
            when (status) {
                ViewEnums.VIEW_ERROR -> {
                    ivStatusImage.setImageResource(R.drawable.ic_status_error)
//                    tvStatusText.text = getString(R.string.there_error)
                    tvStatusMainText.text = getString(R.string.some_thing_wrong)
                }
                ViewEnums.VIEW_EMPTY -> {
                    ivStatusImage.setImageResource(defaultEmptyIcon)
//                    tvStatusText.text = getString(R.string.there_no_data_info)
                    tvStatusMainText.text = getString(defaultEmptyString)

                }
                ViewEnums.VIEW_OFFLINE -> {
                    ivStatusImage.setImageResource(R.drawable.ic_status_offline)
//                    tvStatusText.text = getString(R.string.offline)
                    tvStatusMainText.text = getString(R.string.offlineMain)

                }
                else -> {
                }
            }
        }

    }
}

fun <T> Activity.controlViewsResponse(
    result: Results<T>,
    defaultEmptyIcon: Int,
    defaultEmptyString: Int,
    isShowCreateIcon: Boolean,
    ignoreEmpty: Boolean = false
    ) {

    when (result) {
        is Results.Success -> {
            Log.e("asdasghdjagd","askjdhasdh")
            result.value?.let { returnedValue ->
                if (ignoreEmpty) {
                    controlViews(ViewEnums.VIEW_SHOWING)
                } else
                    when (returnedValue) {
                        is PaginationModel<*> -> {
                            if (returnedValue.data.isEmpty()) {
                                controlViews(ViewEnums.VIEW_EMPTY,defaultEmptyIcon,defaultEmptyString,isShowCreateIcon)
                            } else {
                                controlViews(ViewEnums.VIEW_SHOWING)
                            }
                        }
                        is List<*> -> {
                            if (returnedValue.isEmpty()) {
                                controlViews(ViewEnums.VIEW_EMPTY,defaultEmptyIcon,defaultEmptyString,isShowCreateIcon)
                            } else {
                                controlViews(ViewEnums.VIEW_SHOWING)
                            }
                        }
                        else -> {
                            controlViews(ViewEnums.VIEW_SHOWING)
                        }
                    }
            }
        }
        is Results.Error -> {
            when (result.reason) {
                Results.FailureReason.USER_SIDE -> controlViews(ViewEnums.VIEW_ERROR)
                Results.FailureReason.SERVER_SIDE -> controlViews(ViewEnums.VIEW_ERROR)
                Results.FailureReason.OFFLINE -> controlViews(ViewEnums.VIEW_OFFLINE)
                Results.FailureReason.UNKNOWN_REASON -> controlViews(ViewEnums.VIEW_ERROR)
                Results.FailureReason.UNAUTHORIZED -> {
                    controlViews(ViewEnums.VIEW_ERROR)
                    UseCaseImpl().clearUserData()
                    sStartActivityWithClear<SplashActivity>()
                }

            }
        }
    }
}

fun <T> Activity.controlLoadingResponse(result: Results<T>) {
    when (result) {
        is Results.Success -> {
//            toasting(result.message)
        }
        is Results.Error -> {
            when (result.reason) {
                Results.FailureReason.UNAUTHORIZED -> {
                    toasting(R.string.unAuthorize)
                    UseCaseImpl().clearUserData()
                    sStartActivityWithClear<SplashActivity>()
                }
                Results.FailureReason.USER_SIDE -> {
                    toasting(result.message)
                }
                Results.FailureReason.SERVER_SIDE -> {
                    toasting(R.string.there_error)
                }
                Results.FailureReason.OFFLINE -> {
                    toasting(R.string.offline)
                }
                Results.FailureReason.UNKNOWN_REASON -> {
                    toasting(R.string.there_error_unkown)
                }
            }
        }
    }
}

