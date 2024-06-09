package com.wesal.askhail.core.extentions

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.wesal.askhail.R
import com.wesal.askhail.core.presentationEnums.ViewEnums
import com.wesal.askhail.features.splash.SplashActivity
import com.wesal.domain.core.casesHandler.Results
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.base.PaginationModel

//
fun Fragment.controlViews(
    status: ViewEnums = ViewEnums.VIEW_LOADING,
    defaultEmptyIcon: Int = R.drawable.ic_status_empty_default,
    defaultEmptyString: Int = R.string.empty_default,
    isShowCreateIcon: Boolean = false
) {
    val rootView = this.view

    val viewContainer = rootView?.findViewById<View>(R.id.viewContainer)
    val viewLoading = rootView?.findViewById<View>(R.id.viewLoading)
    val viewStatus = rootView?.findViewById<View>(R.id.viewStatus)
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
            val ivStatusImage = rootView?.findViewById<ImageView>(R.id.ivStatusImage)
            val tvStatusMainText = rootView?.findViewById<TextView>(R.id.tvStatusMainText)
            val fabCreatingStatus = rootView?.findViewById<FloatingActionButton>(R.id.fabCreatingStatus)
            fabCreatingStatus?.let {
                if (isShowCreateIcon){
                    it.visible()
                }else{
                    it.gone()
                }
            }

            when (status) {
                ViewEnums.VIEW_ERROR -> {
                    ivStatusImage?.setImageResource(R.drawable.ic_status_error)
//                    tvStatusText?.text = getString(R.string.there_error)
                    tvStatusMainText?.text = getString(R.string.some_thing_wrong)

                }
                ViewEnums.VIEW_EMPTY -> {
                    ivStatusImage?.setImageResource(defaultEmptyIcon)
//                    tvStatusText?.text = getString(R.string.empty_default)
                    tvStatusMainText?.text = getString(defaultEmptyString)

                }
                ViewEnums.VIEW_OFFLINE -> {
                    ivStatusImage?.setImageResource(R.drawable.ic_status_offline)
//                    tvStatusText?.text = getString(R.string.offline)
                    tvStatusMainText?.text = getString(R.string.offlineMain)
                }
                else -> {
                }
            }
        }

    }
}

fun <T> Fragment.controlViewsResponse(
    result: Results<T>,
    defaultEmptyIcon: Int,
    defaultEmptyString: Int,
    isShowCreateIcon: Boolean
) {
    when (result) {
        is Results.Success -> {
            result.value?.let { returnedValue ->
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
                    context?.sStartActivityWithClear<SplashActivity>()
                }

            }
        }
    }
}

fun <T> Fragment.controlLoadingResponse(result: Results<T>) {
    when (result) {
        is Results.Success -> {
//            requireContext().toasting(result.message)
        }
        is Results.Error -> {
            when (result.reason) {
                Results.FailureReason.UNAUTHORIZED -> {
                    requireContext().toasting(R.string.unAuthorize)
                    UseCaseImpl().clearUserData()
                    context?.sStartActivityWithClear<SplashActivity>()
                }
                Results.FailureReason.USER_SIDE -> {
                    requireContext().toasting(result.message)
                }
                Results.FailureReason.SERVER_SIDE -> {
                    requireContext().toasting(R.string.there_error)
                }
                Results.FailureReason.OFFLINE -> {
                    requireContext().toasting(R.string.offline)
                }
                Results.FailureReason.UNKNOWN_REASON -> {
                    requireContext().toasting(R.string.there_error)
                }
            }
        }
    }
}