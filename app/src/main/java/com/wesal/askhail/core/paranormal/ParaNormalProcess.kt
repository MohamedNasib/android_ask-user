package com.wesal.askhail.core.paranormal

import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.base.BaseFragment
import com.wesal.askhail.core.extentions.controlLoadingResponse
import com.wesal.askhail.core.extentions.controlViews
import com.wesal.askhail.core.extentions.controlViewsResponse
import com.wesal.domain.core.casesHandler.Results
import kotlinx.coroutines.launch
import java.lang.Exception

object ParaNormalProcess {
//
fun <T> loadingProcessActivity(
    parent: BaseActivity,
    executeProcess: suspend () -> Results<T?>,
    isShowLoading: Boolean = true,
    result: (T?) -> Unit
) {
    parent.scopeIO.launch {
        parent.scopeMain.launch {
            if (isShowLoading) {
                parent.attachContextToLoadingDialog(parent)
                parent.showLoadingDialog()
            }
        }
        var data : Results<T?> =Results.Error(Results.FailureReason.UNKNOWN_REASON,"")
        try{
            data = executeProcess.invoke()
        }catch (e: Exception){
            e.printStackTrace()
        }
        parent.scopeMain.launch {
            if (isShowLoading){
                parent.dismissLoadingDialog()
            }
            when (data) {
                is Results.Success -> {
                    result.invoke(data.value)
                }
                else -> {
                }
            }
            parent.controlLoadingResponse(data)
        }
    }
}

    fun <T> viewProcessActivity(
        parent: BaseActivity,
        executeProcess: suspend () -> Results<T?>,
        defaultEmptyIcon:Int= R.drawable.ic_status_empty_default,
        defaultEmptyString:Int = R.string.empty_default,
        isShowCreateIcon:Boolean = false,
        skipEmptyView:Boolean = false,
        result: (T?) -> Unit
    ) {
        parent.scopeIO.launch {
            parent.scopeMain.launch {
                parent.controlViews()
            }
            var data : Results<T?> =Results.Error(Results.FailureReason.UNKNOWN_REASON,"")
            try{
                data = executeProcess.invoke()
            }catch (e: Exception){
                e.printStackTrace()
            }
            parent.scopeMain.launch {
                when (data) {
                    is Results.Success -> {
                        result.invoke(data.value)
                    }
                    else -> {
                    }
                }
                parent.controlViewsResponse(data,defaultEmptyIcon,defaultEmptyString,isShowCreateIcon,skipEmptyView)
            }
        }
    }

    fun <T> viewProcessFragment(
        parent: BaseActivity,
        executeProcess: suspend () -> Results<T?>,
        defaultEmptyIcon:Int= R.drawable.ic_status_empty_default,
        defaultEmptyString:Int = R.string.empty_default,
        isShowCreateIcon:Boolean = false,
        skipEmptyView:Boolean = false,
        result: (T?) -> Unit
    ) {
        parent.scopeIO.launch {
            parent.scopeMain.launch {
                parent.controlViews()
            }
            var data : Results<T?> =Results.Error(Results.FailureReason.UNKNOWN_REASON,"")
            try{
                data = executeProcess.invoke()
            }catch (e: Exception){
                e.printStackTrace()
            }
            parent.scopeMain.launch {
                when (data) {
                    is Results.Success -> {
                        result.invoke(data.value)
                    }
                    else -> {
                    }
                }
                parent.controlViewsResponse(data,defaultEmptyIcon,defaultEmptyString,isShowCreateIcon,skipEmptyView)
            }
        }
    }


    fun <T> silentProcessActivity(
        parent: BaseActivity,
        executeProcess: suspend () -> Results<T?>,
        result: (T?) -> Unit
    ) {
        parent.scopeIO.launch {
            val data = executeProcess.invoke()
            parent.scopeMain.launch {
                when (data) {
                    is Results.Success -> {
                        result.invoke(data.value)
                    }
                    else -> {
                    }
                }
            }
        }
    }
    //////////////////////////////////////////////////////////////////////////////////
    /////////////////////////  FRAGMENT CONTROLLERS  /////////////////////////////////
    /////////////////////////  FRAGMENT CONTROLLERS  /////////////////////////////////
    /////////////////////////  FRAGMENT CONTROLLERS  /////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////
    fun <T> loadingProcessFragment(
        parent: BaseFragment,
        executeProcess: suspend () -> Results<T?>,
        isShowLoading: Boolean = true,
        result: (T?) -> Unit
    ) {
        parent.scopeIO.launch {
            parent.scopeMain.launch {
                if (isShowLoading) {
                    parent.attachContextToLoadingDialog(parent.requireActivity())
                    parent.showLoadingDialog()
                }
            }
            var data : Results<T?> =Results.Error(Results.FailureReason.UNKNOWN_REASON,"")
            try{
                data = executeProcess.invoke()
            }catch (e: Exception){
                e.printStackTrace()
            }
            parent.scopeMain.launch {
                if (isShowLoading){
                    parent.dismissLoadingDialog()
                }
                when (data) {
                    is Results.Success -> {
                        result.invoke(data.value)
                    }
                    else -> {
                    }
                }
                parent.controlLoadingResponse(data)
            }
        }
    }
    fun <T> viewProcessFragment(
        parent: BaseFragment,
        executeProcess: suspend () -> Results<T?>,
        defaultEmptyIcon:Int= R.drawable.ic_status_empty_default,
        defaultEmptyString:Int = R.string.empty_default,
        isShowCreateIcon:Boolean = false,
        result: (T?) -> Unit
    ) {
        parent.scopeIO.launch {
            parent.scopeMain.launch {
                parent.controlViews()
            }
            val data = executeProcess.invoke()
            parent.scopeMain.launch {
                when (data) {
                    is Results.Success -> {
                        result.invoke(data.value)
                    }
                    else -> {
                    }
                }
                parent.controlViewsResponse(data,defaultEmptyIcon,defaultEmptyString,isShowCreateIcon)
            }
        }
    }

    fun <T> silentProcessFragment(
        parent: BaseFragment,
        executeProcess: suspend () -> Results<T?>,
        result: (T?) -> Unit
    ) {
        parent.scopeIO.launch {
            val data = executeProcess.invoke()
            parent.scopeMain.launch {
                when (data) {
                    is Results.Success -> {
                        result.invoke(data.value)
                    }
                    else -> {
                    }
                }
            }
        }
    }

}