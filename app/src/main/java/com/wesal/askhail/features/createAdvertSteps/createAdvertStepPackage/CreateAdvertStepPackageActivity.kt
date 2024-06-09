package com.wesal.askhail.features.createAdvertSteps.createAdvertStepPackage

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.dialogs.AppDialogs
import com.wesal.askhail.core.extentions.*
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.core.presentationEnums.PayingTimeType
import com.wesal.askhail.databinding.ActivityChatBinding
import com.wesal.askhail.databinding.ActivityCreateAdvertStepPackageBinding
import com.wesal.askhail.databinding.ActivitySuccessPageBinding
import com.wesal.askhail.databinding.MyToolbarStepsBinding
import com.wesal.askhail.features.createAdvertSteps.createAdvertStepSection.CreateAdvertStepSectionActivity
import com.wesal.askhail.features.payPackage.payPackageStepSelectType.PayPackageStepSelectTypeActivity
import com.wesal.askhail.features.paymentWebView.PaymentWebViewActivity
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.models.PackageModel
import timber.log.Timber

class CreateAdvertStepPackageActivity : BaseActivity(), AdvertPackagesAdapter.OnPackageClicked {
    lateinit var binding: ActivityCreateAdvertStepPackageBinding
    lateinit var tool_binding: MyToolbarStepsBinding
    override fun layoutResource(): Int = R.layout.activity_create_advert_step_package
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAdvertStepPackageBinding.inflate(layoutInflater)
        tool_binding = MyToolbarStepsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        hideStatusBar()
        setToolbar(getString(R.string.create_new_advert))
        tool_binding.tvSteps.text = "2/6"
        launching()
    }

    private fun launching() {
        ParaNormalProcess.viewProcessActivity(
                this,
                { UseCaseImpl().getPackagesList() }
        ) {
            it?.let {
                binding.rvAdvertPackage.layoutManager = LinearLayoutManager(this)
                val adapter = AdvertPackagesAdapter(it.toMutableList(), this@CreateAdvertStepPackageActivity)
                binding.rvAdvertPackage.adapter = adapter
            }
        }

    }

    override fun onPackageClicked(model: PackageModel) {
        if (model.packagePrice == "0") {
            subscribeToFreePackage(model.packageId)
        } else {
            AppDialogs.paymentOptionsDialog(this) {
                when (it) {
                    PayingTimeType.PAY_NOW -> {
                        sStartActivity<PayPackageStepSelectTypeActivity>(
                                ExtraConst.EXTRA_PACKAGE_MODEL to model
                        )
                    }
                    PayingTimeType.PAY_LATER -> {
                        payLater(model.packageId)
                        Timber.e("PAY_LATER")
                    }
                }
            }.show()
        }
    }

    private fun subscribeToFreePackage(packageId: Int) {
        val map = hashMapOf<String, Any>()
        map["package_id"] = packageId
        map["payment_time"] = "now"
        map["payment_way"] = "no_payment"
        ParaNormalProcess.loadingProcessActivity(
                this,
                { UseCaseImpl().payPackageBackObject(map) }
        ) {
            it?.let { q ->
                toasting(R.string.subscribe_free_package)
                sStartActivityWithClear<CreateAdvertStepSectionActivity>(
                        ExtraConst.EXTRA_ADVERT_ID to q.advertisementId
                )
            }
        }

    }

    private fun payLater(packageId: Int) {
        val map = hashMapOf<String, Any>()
        map["package_id"] = packageId
        map["payment_time"] = "later"
        ParaNormalProcess.loadingProcessActivity(
                this,
                { UseCaseImpl().payPackageBackObject(map) }
        ) {
            it?.let { q ->
                sStartActivityWithClear<CreateAdvertStepSectionActivity>(
                        ExtraConst.EXTRA_ADVERT_ID to q.advertisementId
                )
            }
        }
    }
}