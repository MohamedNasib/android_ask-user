package com.wesal.askhail.features.myPackageSteps.updateSelectPackage

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.extentions.*
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.databinding.ActivityInactiveAdvertListBinding
import com.wesal.askhail.databinding.ActivitySuccessPageBinding
import com.wesal.askhail.databinding.ActivityUpdateSelectPackageBinding
import com.wesal.askhail.features.createAdvertSteps.createAdvertStepPackage.AdvertPackagesAdapter
import com.wesal.askhail.features.createAdvertSteps.createAdvertStepSection.CreateAdvertStepSectionActivity
import com.wesal.askhail.features.payPackage.payPackageStepSelectType.PayPackageStepSelectTypeActivity
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.models.PackageModel

class UpdateSelectPackageActivity : BaseActivity(), AdvertPackagesAdapter.OnPackageClicked {
    lateinit var binding: ActivityUpdateSelectPackageBinding
    override fun layoutResource(): Int = R.layout.activity_update_select_package
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateSelectPackageBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setWhiteActivity()
        setToolbar(getString(R.string.edit_package))
        launching()

    }

    private fun launching() {
        ParaNormalProcess.viewProcessActivity(
            this,
            { UseCaseImpl().getPackagesList() }
        ) {
            it?.let {
                binding.rvAdvertPackage.layoutManager = LinearLayoutManager(this)
                val adapter =
                    AdvertPackagesAdapter(it.toMutableList(), this@UpdateSelectPackageActivity)
                binding.rvAdvertPackage.adapter = adapter
            }
        }

    }

    override fun onPackageClicked(model: PackageModel) {
        if (model.packagePrice == "0") {
            subscribeToFreePackage(model.packageId)
        } else {
            sStartActivity<PayPackageStepSelectTypeActivity>(
                ExtraConst.EXTRA_PACKAGE_MODEL to model,
                ExtraConst.EXTRA_ACTION_TYPE to 2
            )
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
                onBackPressed()
            }
        }
    }

}