package com.wesal.askhail.features.payPackage.payPackageStepSelectBank

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.wesal.askhail.R
import com.wesal.askhail.core.base.BaseActivity
import com.wesal.askhail.core.extentions.sStartActivity
import com.wesal.askhail.core.extentions.setToolbar
import com.wesal.askhail.core.extentions.setWhiteActivity
import com.wesal.askhail.core.paranormal.ParaNormalProcess
import com.wesal.askhail.core.presentationEnums.ExtraConst
import com.wesal.askhail.databinding.ActivityInactiveAdvertListBinding
import com.wesal.askhail.databinding.ActivityPayPackageStepSelectBankBinding
import com.wesal.askhail.databinding.ActivitySuccessPageBinding
import com.wesal.askhail.features.payPackage.payPackageStepSInfoBank.PayPackageStepSInfoBankActivity
import com.wesal.domain.useCases.UseCaseImpl
import com.wesal.entities.models.BanksModel
import com.wesal.entities.models.PackageModel

class PayPackageStepSelectBankActivity : BaseActivity(), BanksAdapter.OnBankClicked {
    lateinit var binding: ActivityPayPackageStepSelectBankBinding
    private var actionType: Int = -1
    private lateinit var packageModel: PackageModel
    override fun layoutResource(): Int =R.layout.activity_pay_package_step_select_bank
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayPackageStepSelectBankBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setToolbar(getString(R.string.pay_package_price))
        setWhiteActivity()
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        packageModel= intent.getParcelableExtra(ExtraConst.EXTRA_PACKAGE_MODEL)!!
        actionType = intent.getIntExtra(ExtraConst.EXTRA_ACTION_TYPE,-1)
        setUpPackage()
        launchingBanks()
    }

    private fun setUpPackage() {
        packageModel.let {
            binding.tvPackageName?.text = it.packageName
            binding.tvPackageDesc.text = it.packageDescription
            binding.tvPackageCount.text = it.packageAdvertisementsCount
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
            {UseCaseImpl().loadBanks()}
        ){
            binding.rvBanks.layoutManager = LinearLayoutManager(this)
            val adapter = BanksAdapter(it!!.toMutableList(),this@PayPackageStepSelectBankActivity)
            binding.rvBanks.adapter=adapter
        }

    }

    override fun onBankClicked(model: BanksModel) {
        sStartActivity<PayPackageStepSInfoBankActivity>(
            ExtraConst.EXTRA_PACKAGE_MODEL to  packageModel,
            ExtraConst.EXTRA_BANK_MODEL to  model,
            ExtraConst.EXTRA_ACTION_TYPE to actionType

        )

    }

}