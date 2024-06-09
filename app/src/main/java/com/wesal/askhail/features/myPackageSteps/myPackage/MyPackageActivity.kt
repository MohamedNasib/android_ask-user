package com.wesal.askhail.features.myPackageSteps.myPackage

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.extentions.*
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.core.presentationEnums.ViewEnums
import com.wesal.askhail.databinding.ActivityInactiveAdvertListBinding
import com.wesal.askhail.databinding.ActivityMyPackageBinding
import com.wesal.askhail.databinding.ActivitySuccessPageBinding
import com.wesal.askhail.features.myPackageSteps.updateSelectPackage.UpdateSelectPackageActivity
import com.wesal.askhail.features.payPackage.payPackageStepSelectType.PayPackageStepSelectTypeActivity
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.models.MyPackageModel
import com.wesal.entities.models.PackageDetailsModel
import com.wesal.entities.models.PackageModel

class MyPackageActivity : BaseActivity() {
    lateinit var binding: ActivityMyPackageBinding
    private var model: MyPackageModel? = null

    override fun layoutResource(): Int = R.layout.activity_my_package
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPackageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setWhiteActivity()
        setToolbar(getString(R.string.acc_mypackages))
        launching()
    }

    private fun launching() {
        ParaNormalProcess.viewProcessActivity(
            this,
            { UseCaseImpl().getMyCurrentPackage() }
        ) {
            controlViews(ViewEnums.VIEW_SHOWING)
            model = it
            updateUi(it)
        }
    }
    private fun updateUi(it: MyPackageModel?) {
        clickers()
        if (it == null) {
            controlNoPackage()
        } else {
            controlExistPackage(it)
        }

    }

    private fun controlExistPackage(it: MyPackageModel) {
        binding.viewHasPackage.visible()
        binding.viewNoPackage.gone()
        val isLater = it.packageIfSubscriptionTypeIsLater
        binding.tvPackageName?.text = it.packageName
        binding.tvPackageDesc.text = it.packageDescription
        binding.tvPackageCount.text = it.packageAdvertisementsCount
        if (it.packagePrice=="0") {
            binding.tvPackagePrice.text = getString(R.string.forfree)

        }else{
            binding.tvPackagePrice.text = it.packagePrice

        }
        binding.tvPackagePeriod.text = it.packageDurationInDays
        val packageDetails = it.packageDetails
        if (packageDetails == null) {
            binding.viewPackageSpecification.gone()
        }else{
            binding.viewPackageSpecification.visible()
            setUpPackageSpecificationView(packageDetails)
        }
        if (isLater){
            binding.tvUnpaidPackage.visible()
        }else{
            binding.tvUnpaidPackage.gone()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUpPackageSpecificationView(packageDetails: PackageDetailsModel) {
        binding.tvPackageAdvs.text = "${model?.packageAdvertisementsCount} ${getString(R.string.advert)}"
        binding.tvUsedAds.text = "${packageDetails?.subscriptionUsedAdvertisements} ${getString(R.string.advert)}"
        binding.tvBuyDate.text = packageDetails.subscriptionCustomStartDate
        binding.tvRenewDate.text = packageDetails.subscriptionCustomEndDate
        binding.tvRemainDays.text = "${packageDetails.subscriptionRestDays} ${getString(R.string.day)}"
        if (packageDetails.subscriptionCanRenewPackageStatus){
            binding.tvRenewPackage.visible()
            binding.viewShowAlert.visible()
//            binding.tvRemainDays.setTextColor(Color.parseColor("#FF7A94"))
        }else{
            binding.tvRenewPackage.gone()
            binding.viewShowAlert.gone()
//            binding.tvRemainDays.setTextColor(Color.parseColor("#034B89"))

        }
    }

    private fun controlNoPackage() {
        Log.e("controlNoPackage","controlNoPackage")
        binding.viewHasPackage.gone()
        binding.viewNoPackage.visible()
    }

    private fun clickers() {
        binding.tvUnpaidPackage.setOnClickListener { payForCurrentPackage() }
        binding.tvRenewPackage.setOnClickListener { payForCurrentPackage() }
        binding.tvUpgradePackage.setOnClickListener { sStartActivity<UpdateSelectPackageActivity>() }

    }

    private fun payForCurrentPackage() {
        val packageModel = PackageModel(
            model!!.packageId,
            model!!.packageName,
            model!!.packageDescription,
            "",
            model!!.packageRate,
            model!!.packageDurationInDays,
            model!!.packagePrice,
            model!!.packageAdvertisementsCount
        )
        sStartActivity<PayPackageStepSelectTypeActivity>(
            ExtraConst.EXTRA_PACKAGE_MODEL to packageModel,
            ExtraConst.EXTRA_ACTION_TYPE to 1
        )

    }

}