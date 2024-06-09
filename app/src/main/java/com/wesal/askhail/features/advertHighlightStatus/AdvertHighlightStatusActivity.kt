package com.wesal.askhail.features.advertHighlightStatus

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.extentions.gone
import com.wesal.askhail.core.extentions.setToolbar
import com.wesal.askhail.core.extentions.setWhiteActivity
import com.wesal.askhail.core.extentions.visible
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.databinding.ActivityAdvertHighlightStatusBinding
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.models.MyPackageModel
import com.wesal.entities.models.PackageDetailsModel

class AdvertHighlightStatusActivity : BaseActivity() {
    lateinit var binding: ActivityAdvertHighlightStatusBinding;
    private var model: MyPackageModel? = null
    private var advertId: Int = -1
    override fun layoutResource(): Int =R.layout.activity_advert_highlight_status
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdvertHighlightStatusBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        advertId = intent.getIntExtra(ExtraConst.EXTRA_ADVERT_ID,-1)
        setWhiteActivity()
        setToolbar(getString(R.string.ads_specialing))
        launching()
    }
    private fun launching() {
        ParaNormalProcess.viewProcessActivity(
            this,
            { UseCaseImpl().getAdvertSpecialPackageInfo(advertId) }
        ) {
            model = it
            if (it != null) {
                updateUi(it)
            }
        }
    }

    private fun updateUi(it: MyPackageModel) {
        binding.viewHasPackage.visible()
        binding.viewNoPackage.gone()
        val isLater = it.packageIfSubscriptionTypeIsLater
        binding.tvPackageName?.text = it.packageName
        binding.tvPackageDesc.text = it.packageDescription
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
        binding.tvBuyDate.text = packageDetails.subscriptionCustomStartDate
        binding.tvRenewDate.text = packageDetails.subscriptionCustomEndDate
        binding.tvRemainDays.text = "${packageDetails.subscriptionRestDays} ${getString(R.string.day)}"
    }

}