package com.wesal.askhail

import android.app.Application
import com.wesal.domain.Domain
import timber.log.Timber


class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Domain.integrateWith(this)
//        controllingDarkMode()
        initTimber()


    }


    private fun initTimber() {

        if (false) {
            Timber.plant(Timber.DebugTree())
//            Timber.plant(object : Timber.DebugTree() {
//                override fun createStackElementTag(element: StackTraceElement): String? {
//                    return String.format(
//                        "Class:%s: Line: %s, Method: %s",
//                        super.createStackElementTag(element),
//                        element.lineNumber,
//                        element.methodName
//                    )
//                }
//            })
        } else {
//            Timber.plant(ReleaseTree())
        }
    }

    private fun controllingDarkMode() {
//        if (preferencesGateway.isDarkMode()) {
//            AppCompatDelegate.setDefaultNightMode(
//                AppCompatDelegate.MODE_NIGHT_YES
//            )
//        } else {
//            AppCompatDelegate.setDefaultNightMode(
//                AppCompatDelegate.MODE_NIGHT_NO
//            )
//        }

        //MODE_NIGHT_NO. Always use the day (light) theme.
        //MODE_NIGHT_YES. Always use the night (dark) theme.
    }
}