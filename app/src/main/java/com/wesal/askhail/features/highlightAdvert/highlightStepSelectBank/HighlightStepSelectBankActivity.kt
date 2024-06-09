package com.wesal.askhail.features.highlightAdvert.highlightStepSelectBank

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.extentions.sStartActivity
import com.wesal.askhail.core.extentions.setToolbar
import com.wesal.askhail.core.extentions.setWhiteActivity
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.databinding.ActivityHightlightStepSelectBankBinding
import com.wesal.askhail.databinding.MyToolbarStepsBinding
import com.wesal.askhail.features.highlightAdvert.highlightStepSInfoBank.HighlightStepSInfoBankActivity
import com.wesal.askhail.features.payPackage.payPackageStepSelectBank.BanksAdapter
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.models.BanksModel
import com.wesal.entities.models.PackageModel

class HighlightStepSelectBankActivity : BaseActivity(), BanksAdapter.OnBankClicked {
    lateinit var binding: ActivityHightlightStepSelectBankBinding
    lateinit var tool_binding: MyToolbarStepsBinding
    private var advertId: Int = -1
    private lateinit var packageModel: PackageModel
    override fun layoutResource(): Int =R.layout.activity_hightlight_step_select_bank
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHightlightStepSelectBankBinding.inflate(layoutInflater)
        tool_binding = MyToolbarStepsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setWhiteActivity()
        setToolbar(getString(R.string.highlight_advert))
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        packageModel= intent.getParcelableExtra(ExtraConst.EXTRA_PACKAGE_MODEL)!!
        advertId = intent.getIntExtra(ExtraConst.EXTRA_ADVERT_ID,-1)
        setUpPackage()
        launchingBanks()
    }
    private fun setUpPackage() {
        packageModel.let {
            binding.tvPackageName?.text = it.packageName
            binding.tvPackageDesc.text = it.packageDescription
            if (it.packagePrice=="0") {
                binding.tvPackagePrice.text = getString(R.string.forfree)
            }else{
                binding.tvPackagePrice.text = it.packagePrice
            }
            binding.tvPackagePeriod.text = it.packageDurationInDays
        }

    }
    private fun launchingBanks() {
        ParaNormalProcess.viewProcessActivity(
            this,
            { UseCaseImpl().loadBanks()}
        ){
            binding.rvBanks.layoutManager = LinearLayoutManager(this)
            val adapter = BanksAdapter(it!!.toMutableList(),this@HighlightStepSelectBankActivity)
            binding.rvBanks.adapter=adapter
        }

    }

    override fun onBankClicked(model: BanksModel) {
        sStartActivity<HighlightStepSInfoBankActivity>(
            ExtraConst.EXTRA_PACKAGE_MODEL to  packageModel,
            ExtraConst.EXTRA_BANK_MODEL to  model,
            ExtraConst.EXTRA_ADVERT_ID to advertId
        )
    }

}