package com.wesal.askhail.features.highlightAdvert.highlightStepPackages

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.extentions.sStartActivity
import com.wesal.askhail.core.extentions.setToolbar
import com.wesal.askhail.core.extentions.setWhiteActivity
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.databinding.ActivityChatBinding
import com.wesal.askhail.databinding.ActivityConfirmPhoneBinding
import com.wesal.askhail.databinding.ActivityHighlightStepPackagesBinding
import com.wesal.askhail.databinding.ActivitySuccessPageBinding
import com.wesal.askhail.databinding.MyToolbarStepsBinding
import com.wesal.askhail.features.createAdvertSteps.createAdvertStepPackage.AdvertPackagesAdapter
import com.wesal.askhail.features.highlightAdvert.highlightStepSelectType.HighlightStepSelectTypeActivity
import com.wesal.askhail.features.payPackage.payPackageStepSelectType.PayPackageStepSelectTypeActivity
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.models.PackageModel

class HighlightStepPackagesActivity : BaseActivity(), AdvertPackagesAdapter.OnPackageClicked {
    lateinit var binding: ActivityHighlightStepPackagesBinding
    lateinit var tool_binding: MyToolbarStepsBinding
    private var advertId: Int = -1
    private var advertPlace: String?= ""
    override fun layoutResource(): Int =R.layout.activity_highlight_step_packages
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHighlightStepPackagesBinding.inflate(layoutInflater)
        tool_binding = MyToolbarStepsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        advertId = intent.getIntExtra(ExtraConst.EXTRA_ADVERT_ID,-1)
        advertPlace = intent.getStringExtra(ExtraConst.EXTRA_ADVERT_PLACE)
        setWhiteActivity()
        setToolbar(getString(R.string.highlight_advert))
        launching()
    }

    private fun launching() {

        ParaNormalProcess.viewProcessActivity(
            this,
            {UseCaseImpl().getHighlightPackages(advertPlace!!)}
        ){
            it?.let { it1 -> updateUi(it1) }
        }
    }

    private fun updateUi(it: List<PackageModel>) {
        binding.rvHighlightPackages.layoutManager = LinearLayoutManager(this)
        val adapter = AdvertPackagesAdapter(it.toMutableList(), this@HighlightStepPackagesActivity,true)
        binding.rvHighlightPackages.adapter = adapter
    }

    override fun onPackageClicked(model: PackageModel) {
        sStartActivity<HighlightStepSelectTypeActivity>(
            ExtraConst.EXTRA_PACKAGE_MODEL to model,
            ExtraConst.EXTRA_ADVERT_ID to advertId
        )

    }
}