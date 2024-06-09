package com.wesal.askhail.features.main

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.dialogs.AppDialogs
import com.wesal.askhail.core.extentions.requiredLoginArea
import com.wesal.askhail.core.extentions.sStartActivity
import com.wesal.askhail.core.extentions.sStartActivityWithClear
import com.wesal.askhail.core.extentions.setWhiteActivity
import com.wesal.askhail.core.firebase.FirebaseTokenManager
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.presentationEnums.AppContentsType
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.databinding.ActivityMainBinding
import com.wesal.askhail.features.createAdvertSteps.createAdvertStepContact.CreateAdvertStepContactActivity
import com.wesal.askhail.features.createAdvertSteps.createAdvertStepInfo.CreateAdvertStepInfoActivity
import com.wesal.askhail.features.createAdvertSteps.createAdvertStepMedia.CreateAdvertStepMediaActivity
import com.wesal.askhail.features.createAdvertSteps.createAdvertStepSection.CreateAdvertStepSectionActivity
import com.wesal.askhail.features.createAdvertSteps.createAdvertStepSpecification.CreateAdvertStepSpecificationActivity
import com.wesal.askhail.features.createAskHail.createAskHailStepInfo.CreateAskHailStepInfoActivity
import com.wesal.askhail.features.createOrderSteps.createAdvertStepInfo.CreateOrderStepInfoActivity
import com.wesal.askhail.features.createOrderSteps.createOrderStepContact.CreateOrderStepContactActivity
import com.wesal.askhail.features.createOrderSteps.createOrderStepSection.CreateOrderStepSectionActivity
import com.wesal.askhail.features.createOrderSteps.createOrderStepSpecification.CreateOrderStepSpecificationActivity
import com.wesal.askhail.features.more.MoreFragment
import com.wesal.askhail.features.notifications.NotificationsActivity
import com.wesal.askhail.features.prayAndWeather.PrayAndWeatherActivity
import com.wesal.askhail.features.splash.SplashActivity
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.models.AdvertStepperModel
import com.wesal.entities.models.OrderStepperModel
import kotlinx.coroutines.launch


class MainActivity : BaseActivity() {
    lateinit var binding: ActivityMainBinding
    public lateinit var navController: NavController

    var mCartItemCount = 0
    override fun layoutResource(): Int = R.layout.activity_main
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupBadge()

        setWhiteActivity()
        setUpView()
        clicker()
    }

    private fun setupBadge() {
        ParaNormalProcess.silentProcessActivity(
            this,
            { UseCaseImpl().getUnreadNotifications() }
        ) {
            if (it != null) {
                mCartItemCount = it.toInt()
                if (mCartItemCount == 0) {
                    if (binding.notificationBadge.visibility != View.GONE) {
                        binding.notificationBadge.visibility = View.GONE
                    }
                } else {
                    binding.notificationBadge.text = mCartItemCount.coerceAtMost(99).toString()
                    if (binding.notificationBadge.visibility != View.VISIBLE) {
                        binding.notificationBadge.visibility = View.VISIBLE
                    }
                }
            }

        }
        if (mCartItemCount == 0) {
            if (binding.notificationBadge.visibility != View.GONE) {
                binding.notificationBadge.visibility = View.GONE
            }
        } else {
            binding.notificationBadge.text = mCartItemCount.coerceAtMost(99).toString()
            if (binding.notificationBadge.visibility != View.VISIBLE) {
                binding.notificationBadge.visibility = View.VISIBLE
            }
        }

    }

    override fun onResume() {
        super.onResume()
        ParaNormalProcess.silentProcessActivity(
            this,
            { UseCaseImpl().getFixedPages() }
        ) {

        }
        setupBadge()
    }

    private fun setUpView() {
        navController = findNavController(R.id.nav_host_fragment)
        navController.setGraph(R.navigation.nav_home)
        navController.navigate(R.id.homeFragment)

    }

    private fun clicker() {

        lifecycleScope.launch {
            FirebaseTokenManager().isValidToken().let {
                if (!it) {
                    UseCaseImpl().clearUserData()
                    sStartActivityWithClear<SplashActivity>()
                    scopeIO.launch {
                        UseCaseImpl().logOut()
                    }
                }
            }

        }
        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> startNavigation(R.id.homeFragment)
                R.id.nav_messages -> startNavigation(R.id.messagesFragment)
                R.id.nav_ask -> startNavigation(R.id.askHailFragment)
                R.id.nav_more -> {
                    showMoreMenu()
                    return@setOnNavigationItemSelectedListener false
                }
            }
            true
        }

        binding.fabAddNew.setOnClickListener {

            requiredLoginArea(binding.fabAddNew, true) {
                AppDialogs.createNewHomeDialog(this) { type ->
                    when (type) {
                        AppContentsType.ADVERT -> {
                            creatingNewAdvertLogic()
                        }
                        AppContentsType.ORDER -> {
                            checkIfThereOldOrder()
                        }
                        AppContentsType.QUESTION -> {
                            sStartActivity<CreateAskHailStepInfoActivity>()
                        }
                    }
                }.show()
            }
        }
        binding.ivPray.setOnClickListener {
            requestPermissionLocationLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        binding.ivNotification.setOnClickListener {
            requiredLoginArea(binding.ivNotification, false) {
                sStartActivity<NotificationsActivity>()

            }
        }
    }

    private val requestPermissionLocationLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                sStartActivity<PrayAndWeatherActivity>()
            }
        }

    private fun creatingNewAdvertLogic() {
        ParaNormalProcess.loadingProcessActivity(
            this,
            { UseCaseImpl().checkDraftAdverts() }
        ) { checkModel ->
            if (checkModel == null) {
                sStartActivity<CreateAdvertStepInfoActivity>()
            } else {
                AppDialogs.checkDraftDialog(this) { isNewAdvert: Boolean ->
                    if (isNewAdvert) {
                        makeNewAdvertId()
                    } else {
                        routeToCreateAdvertStep(checkModel)
                    }
                }.show()
            }
        }
    }

    private fun makeNewAdvertId() {
        ParaNormalProcess.loadingProcessActivity(
            this,
            { UseCaseImpl().makeNewAdvertId() }
        ) {
            sStartActivity<CreateAdvertStepInfoActivity>()
        }
    }

    private fun routeToCreateAdvertStep(checkModel: AdvertStepperModel) {
        val advertisementId = checkModel.advertisementId
        when (checkModel.nextLevel) {
            3 -> sStartActivity<CreateAdvertStepSectionActivity>(ExtraConst.EXTRA_ADVERT_ID to advertisementId)
            4 -> sStartActivity<CreateAdvertStepMediaActivity>(ExtraConst.EXTRA_ADVERT_ID to advertisementId)
            5 -> sStartActivity<CreateAdvertStepSpecificationActivity>(ExtraConst.EXTRA_ADVERT_ID to advertisementId)
            6 -> sStartActivity<CreateAdvertStepContactActivity>(ExtraConst.EXTRA_ADVERT_ID to advertisementId)
        }
    }

    private fun checkIfThereOldOrder() {
        ParaNormalProcess.loadingProcessActivity(
            this,
            { UseCaseImpl().createOrderStepCheckPage() }
        ) { checkModel ->
            if (checkModel == null) {
                sStartActivity<CreateOrderStepInfoActivity>()
            } else {
                AppDialogs.checkDraftDialog(this@MainActivity) { isNewOrder ->
                    if (isNewOrder) {
                        makeNewOrderId()
                    } else {
                        routeToCreateOrderStep(checkModel)
                    }
                }.show()
            }
        }

    }

    private fun makeNewOrderId() {
        ParaNormalProcess.loadingProcessActivity(
            this,
            { UseCaseImpl().makeNewOrderId() }
        ) {
            if (it != null) {
                sStartActivity<CreateOrderStepSectionActivity>(
                    ExtraConst.EXTRA_ORDER_ID to it.orderId
                )
            }
        }
    }

    private fun routeToCreateOrderStep(checkModel: OrderStepperModel) {
        val orderId = checkModel.orderId
        when (checkModel.nextLevel) {
            2 -> sStartActivity<CreateOrderStepSectionActivity>(ExtraConst.EXTRA_ORDER_ID to orderId)
            3 -> sStartActivity<CreateOrderStepSpecificationActivity>(ExtraConst.EXTRA_ORDER_ID to orderId)
            4 -> sStartActivity<CreateOrderStepContactActivity>(ExtraConst.EXTRA_ORDER_ID to orderId)
        }
    }

    private fun startNavigation(fragmentId: Int) {
        if (navController.currentDestination?.id == fragmentId) {
            return
        } else {
            navController.navigate(fragmentId)
        }
    }

    private fun showMoreMenu() {
        val moreFragment = MoreFragment()
        moreFragment.show(supportFragmentManager, moreFragment.tag)
    }

}