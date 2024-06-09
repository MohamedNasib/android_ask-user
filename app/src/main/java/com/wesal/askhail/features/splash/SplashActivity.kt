package com.wesal.askhail.features.splash

import android.Manifest
import android.animation.Animator
import android.annotation.SuppressLint
import android.app.TaskStackBuilder
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.messaging.FirebaseMessaging
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.extentions.sStartActivityWithClear
import com.wesal.askhail.core.extentions.toasting
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.core.utilities.LocationLiveData
import com.wesal.askhail.core.utilities.ShareManger
import com.wesal.askhail.features.advertDetails.AdvertDetailsActivity
import com.wesal.askhail.features.askHailDetails.AskHailDetailsActivity
import com.wesal.askhail.features.browsingType.BrowsingTypeActivity
import com.wesal.askhail.features.main.MainActivity
import com.wesal.askhail.features.orderDetails.OrderDetailsActivity
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.enums.EStartRoute
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception
import androidx.lifecycle.observe
import com.wesal.askhail.core.utilities.GpsRequester
import com.wesal.askhail.databinding.ActivityPickLocationBinding
import com.wesal.askhail.databinding.ActivitySplashBinding
import com.wesal.askhail.databinding.ActivitySuccessPageBinding
import com.wesal.askhail.features.advertHighlightStatus.AdvertHighlightStatusActivity
import com.wesal.askhail.features.myPackageSteps.myPackage.MyPackageActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {
    lateinit var binding: ActivitySplashBinding
    private val delayTime = 1000 * 1L;
    override fun layoutResource(): Int = R.layout.activity_splash

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initFirebaseTopics()
        scopeMain.launch {
            binding.ivLottie.addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator) {
                }

                override fun onAnimationEnd(animation: Animator) {
                    Timber.e("Pausing ==== addAnimatorUpdateListener")
                    detectFromWhere()
                }

                override fun onAnimationCancel(animation: Animator) {
                }

                override fun onAnimationStart(animation: Animator) {
                }

            })
        }

    }
    private fun initFirebaseTopics() {
        val systemLanguage = UseCaseImpl().getSystemLanguage()
        if (systemLanguage == "ar") {
            FirebaseMessaging.getInstance()
                .unsubscribeFromTopic("normal_advertisers_en")
            FirebaseMessaging.getInstance()
                .subscribeToTopic("normal_advertisers_ar")
            FirebaseMessaging.getInstance()
                .unsubscribeFromTopic("all_advertisers_en")
            FirebaseMessaging.getInstance()
                .subscribeToTopic("all_advertisers_ar")
        } else {
            FirebaseMessaging.getInstance()
                .unsubscribeFromTopic("normal_advertisers_ar")
            FirebaseMessaging.getInstance()
                .subscribeToTopic("normal_advertisers_en")
            FirebaseMessaging.getInstance()
                .unsubscribeFromTopic("all_advertisers_ar")
            FirebaseMessaging.getInstance()
                .subscribeToTopic("all_advertisers_en")
        }

    }

    private fun detectFromWhere() {
        Timber.e("ROUTE => detectFromWhere")
        val bundle = intent.extras
        if (bundle != null) {
            val type = bundle.getString("type")
            val relatedId = bundle.getString("type_id")
            when (type) {
                "adv" -> {
                    Timber.e("ROUTE => detectFromWhere ==1")
                    TaskStackBuilder.create(applicationContext)
                        .addNextIntentWithParentStack(
                            Intent(
                                this@SplashActivity,
                                AdvertDetailsActivity::class.java
                            ).putExtra(ExtraConst.EXTRA_ADVERT_ID, relatedId?.toInt())
                        ).startActivities()
                }
                "question" -> {
                    TaskStackBuilder.create(applicationContext)
                        .addNextIntentWithParentStack(
                            Intent(
                                this@SplashActivity,
                                AskHailDetailsActivity::class.java
                            ).putExtra(ExtraConst.EXTRA_ASK_ID, relatedId?.toInt())
                        ).startActivities()
                }
                "special_subscription" -> {
                    TaskStackBuilder.create(applicationContext)
                        .addNextIntentWithParentStack(
                            Intent(
                                this@SplashActivity,
                                AdvertHighlightStatusActivity::class.java
                            ).putExtra(ExtraConst.EXTRA_ADVERT_ID, relatedId?.toInt())
                        ).startActivities()
                }
                "main_subscription " -> {
                    TaskStackBuilder.create(applicationContext)
                        .addNextIntentWithParentStack(
                            Intent(
                                this@SplashActivity,
                                MyPackageActivity::class.java
                            ).putExtra(ExtraConst.EXTRA_ADVERT_ID, relatedId?.toInt())
                        ).startActivities()
                }
                else -> {
                    dynamicLinksRoute()
                }
            }
//            val notifiableId = bundle.getString("notifiable_id")
//            when (type) {
//                2 -> {
//                    TaskStackBuilder.create(applicationContext)
//                        .addNextIntentWithParentStack(
//                            Intent(this@SplashActivity, ProductDetailsActivity::class.java)
//                                .putExtra(ExtraConst.EXTRA_PROD_ID, notifiableId?.toInt())
//                        ).startActivities()
//                    finish()
//
//                }
//                3 -> {
//                    TaskStackBuilder.create(applicationContext)
//                        .addNextIntentWithParentStack(
//                            Intent(this@SplashActivity, OrderDetailsActivity::class.java)
//                                .putExtra(ExtraConst.EXTRA_ORDER_ID, notifiableId?.toInt())
//                        ).startActivities()
//                    finish()
//
//                }
//                else -> {
//                    getStartRoute()
//                }
//            }
        } else {
            dynamicLinksRoute()
        }

    }

    private fun dynamicLinksRoute() {
        Timber.e("ROUTE => dynamicLinksRoute")

        FirebaseDynamicLinks.getInstance()
            .getDynamicLink(intent)
            .addOnSuccessListener(
                this
            ) { pendingDynamicLinkData ->
                val deepLink: Uri?
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link
                    Timber.e("%s", deepLink.toString())

                    try {
                        val queryId = deepLink?.lastPathSegment?.toInt()
                        val path = deepLink?.pathSegments!![deepLink.pathSegments.size - 2]
                        when (path) {
                            ShareManger.QUERY_ADVERT -> {
                                TaskStackBuilder.create(applicationContext)
                                    .addNextIntentWithParentStack(
                                        Intent(
                                            this@SplashActivity,
                                            AdvertDetailsActivity::class.java
                                        ).putExtra(ExtraConst.EXTRA_ADVERT_ID, queryId)
                                    ).startActivities()
                            }
                            ShareManger.QUERY_ORDER -> {
                                TaskStackBuilder.create(applicationContext)
                                    .addNextIntentWithParentStack(
                                        Intent(
                                            this@SplashActivity,
                                            OrderDetailsActivity::class.java
                                        ).putExtra(ExtraConst.EXTRA_ORDER_ID, queryId)
                                    ).startActivities()
                            }
                            ShareManger.QUERY_QUESTION -> {
                                TaskStackBuilder.create(applicationContext)
                                    .addNextIntentWithParentStack(
                                        Intent(
                                            this@SplashActivity,
                                            AskHailDetailsActivity::class.java
                                        ).putExtra(ExtraConst.EXTRA_ASK_ID, queryId)
                                    ).startActivities()
                            }
                        }
                    } catch (e: Exception) {
                        normalRoute()
                    }
                } else {
                    normalRoute()
                }

            }
            .addOnFailureListener(
                this
            ) { e ->
                normalRoute()
            }
    }

    private fun normalRoute() {
        Timber.e("ROUTE => normalRoute")

        when (UseCaseImpl().getStartRoute()) {
            EStartRoute.LOGIN_ROUTE -> {
                sStartActivityWithClear<BrowsingTypeActivity>()
            }
            EStartRoute.USER_ROUTE,EStartRoute.VISITOR_ROUTE -> {
                getCurrentLocationAndGoHome()
            }
        }

    }

    private val requestPermissionLocationLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                GpsRequester(this@SplashActivity)
                LocationLiveData(this@SplashActivity, false)
                    .observe(this@SplashActivity) { location ->
                        if (location != null) {
                            val sydney = LatLng(location.latitude, location.longitude)
                            Timber.e("CurrentLocation ==> lat is ${location.latitude} lng is ${location.longitude}")
                            UseCaseImpl().updateUserLocation(location.latitude, location.longitude)
                        }
                    }
                sStartActivityWithClear<MainActivity>()
//                UseCaseImpl().updateUserLocation(31.001, 30.001)
//                sStartActivityWithClear<MainActivity>()
            } else {
                toasting(R.string.permission_location_required)
            }
        }

    private fun getCurrentLocationAndGoHome() {
        requestPermissionLocationLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }
}