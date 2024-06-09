package com.wesal.askhail.core.base

import android.content.ComponentName
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.kaopiz.kprogresshud.KProgressHUD
import com.wesal.askhail.core.extentions.getLoadingDialog
import com.wesal.domain.useCases.UseCaseImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job


abstract class BaseActivity : AppCompatActivity() {

    private val job = Job()
    val scopeMain = CoroutineScope(job + Dispatchers.Main)
    val scopeIO = CoroutineScope(job + Dispatchers.IO)

    private var loadingDialog: KProgressHUD? = null
    // region Bug In DarkMode With Language
    private var activityHandlesUiMode = false
    private var activityHandlesUiModeChecked = false
    private val isActivityManifestHandlingUiMode: Boolean
        get() {
            if (!activityHandlesUiModeChecked) {
                val pm = packageManager ?: return false
                activityHandlesUiMode = try {
                    val info = pm.getActivityInfo(ComponentName(this, javaClass), 0)
                    info.configChanges and ActivityInfo.CONFIG_UI_MODE != 0
                } catch (e: PackageManager.NameNotFoundException) {
                    false
                }
            }
            activityHandlesUiModeChecked = true
            return activityHandlesUiMode
        }

    //endregion
    @LayoutRes
    protected abstract fun layoutResource(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
        //setContentView(layoutResource())

    }
    override fun onResume() {
        super.onResume()
     }

    override fun attachBaseContext(newBase: Context) {
//        runBlocking {
        val systemLanguage = UseCaseImpl().getSystemLanguage()
        super.attachBaseContext(MyContextWrapper.wrap(newBase, systemLanguage))
//        }


    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        if (overrideConfiguration != null) {
            val uiMode = overrideConfiguration.uiMode
            overrideConfiguration.setTo(baseContext.resources.configuration)
            overrideConfiguration.uiMode = uiMode
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        if (isActivityManifestHandlingUiMode) {
            val nightMode = if (delegate.localNightMode != AppCompatDelegate.MODE_NIGHT_UNSPECIFIED)
                delegate.localNightMode
            else
                AppCompatDelegate.getDefaultNightMode()
            val configNightMode = when (nightMode) {
                AppCompatDelegate.MODE_NIGHT_YES -> Configuration.UI_MODE_NIGHT_YES
                AppCompatDelegate.MODE_NIGHT_NO -> Configuration.UI_MODE_NIGHT_NO
                else -> Configuration.UI_MODE_NIGHT_NO
            }
            newConfig.uiMode = configNightMode or (newConfig.uiMode and Configuration.UI_MODE_NIGHT_MASK.inv())
        }
        super.onConfigurationChanged(newConfig)
    }


    fun attachContextToLoadingDialog(context: Context) {
        loadingDialog = getLoadingDialog(context)
    }

    fun showLoadingDialog() {
        loadingDialog?.show()
    }

    fun dismissLoadingDialog() {
        loadingDialog?.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        loadingDialog?.let {
            it.dismiss()
            loadingDialog = null
        }
        job.cancel()
    }
}